package com.topband.shop.consumer.controller;

import com.alibaba.excel.EasyExcel;
import com.topband.shop.api.GoodsService;
import com.topband.shop.api.UploadFileService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.config.CustomValidatedGroup;
import com.topband.shop.consumer.anno.limit.RateLimiter;
import com.topband.shop.consumer.excel.CheckHeadListener;
import com.topband.shop.consumer.excel.UploadGoodsListener;
import com.topband.shop.consumer.anno.log.LogEnum;
import com.topband.shop.consumer.anno.log.LogRecordAnno;
import com.topband.shop.dto.GoodsQueryDTO;
import com.topband.shop.entity.Goods;
import com.topband.shop.entity.UploadFile;
import com.topband.shop.exception.ShopCustomException;
import com.topband.shop.utils.AdminHolder;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.GoodsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.BusinessConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsController
 * @packageName: com.topband.shop.consumer.controller
 * @description: GoodsController
 * @date 2022/9/12 13:41
 */
@Slf4j
@RestController
@RequestMapping("/goods")
@Api(tags = "商品接口")
public class GoodsController {
    @DubboReference
    private GoodsService goodsService;

    @DubboReference
    private UploadFileService uploadFileService;

    private static final ExecutorService HANDLE_EXCEL_IMPORT_POOL = Executors.newFixedThreadPool(3);

    @PostMapping("/client/list")
    @ApiOperation("c 端分页条件查询商品")
    public Result listForClient(@RequestBody GoodsQueryDTO goodsQueryDTO) {
        return Result.ok(goodsService.list(goodsQueryDTO));
    }

    @PostMapping("/web/list")
    @ApiOperation("web 端分页条件查询商品")
    public Result listForWeb(@RequestBody GoodsQueryDTO goodsQueryDTO) {
        return Result.ok(goodsService.list(goodsQueryDTO));
    }

    @GetMapping("/edit/{goodsId}")
    @ApiOperation("根据 id 查找商品信息")
    public Result findById(@PathVariable("goodsId") Long goodsId) {
        if (goodsId == null) {
            return Result.fail();
        }
        GoodsVO goodsVO = goodsService.selectById(goodsId);
        if (goodsVO == null) {
            return Result.fail();
        }
        return Result.ok(goodsVO);
    }

    @PostMapping("/add")
    @ApiOperation("添加商品接口")
    @LogRecordAnno(value = LogEnum.ADD_GOODS, content = "#goodsVO.name")
    public Result add(@Validated(CustomValidatedGroup.Crud.Create.class) GoodsVO goodsVO,
                      @RequestParam("goodsImg") MultipartFile goodsImg) throws IOException {
        File goodsImgFile = null;
        // 校验图片格式和图片大小
        if (goodsImg != null && !goodsImg.isEmpty()) {
            if (isInValidImgType(goodsImg)) {
                return Result.fail(ResultCodeEnum.IMAGE_PATTERN_ERROR);
            }
            if (isInValidFileSize(goodsImg, IMAGE_MAX_SIZE, true)) {
                return Result.fail(ResultCodeEnum.IMAGE_MAX_SIZE_ERROR);
            }
            String renameFileName = renameFileName(Objects.requireNonNull(goodsImg.getOriginalFilename()));
            // 保存图片
            goodsImgFile = saveGoodsImage(goodsImg, renameFileName);
            String goodsImgPathName = getGoodsImgSavePath(renameFileName);
            goodsVO.setImageUrl(goodsImgPathName);
        }
        Result result = goodsService.save(goodsVO);
        deleteFileIfSaveOrUpdateFail(goodsImgFile, result);
        return result;
    }

    @PutMapping("/update")
    @ApiOperation("根据 id 更新商品信息")
    @LogRecordAnno(LogEnum.UPDATE_OR_DELETE_GOODS)
    public Result updateById(@Validated(CustomValidatedGroup.Crud.Update.class) GoodsVO goodsVO,
                             @RequestParam("goodsImg") MultipartFile updateGoodsImg) throws IOException {
        File updateGoodsImgFile = null;
        String oldGoodsImgPath = null;
        // 校验图片格式和图片大小
        if (updateGoodsImg != null && !updateGoodsImg.isEmpty()) {
            if (isInValidImgType(updateGoodsImg)) {
                return Result.fail(ResultCodeEnum.IMAGE_PATTERN_ERROR);
            }
            if (isInValidFileSize(updateGoodsImg, IMAGE_MAX_SIZE, true)) {
                return Result.fail(ResultCodeEnum.IMAGE_MAX_SIZE_ERROR);
            }
            String renameFileName = renameFileName(Objects.requireNonNull(updateGoodsImg.getOriginalFilename()));
            // 更新图片
            updateGoodsImgFile = saveGoodsImage(updateGoodsImg, renameFileName);
            String goodsImgPathName = getGoodsImgSavePath(renameFileName);
            goodsVO.setImageUrl(goodsImgPathName);
            oldGoodsImgPath = goodsService.selectById(goodsVO.getId()).getImageUrl();
        }
        Result result = goodsService.updateById(goodsVO);
        deleteFileIfSaveOrUpdateFail(updateGoodsImgFile, result);
        // 更新成功，删除原来的图片
        if (result.getCode() == 0 && oldGoodsImgPath != null) {
            File file = new File(System.getProperty("user.dir") + oldGoodsImgPath);
            if (file.exists()) {
                file.delete();
            }
        }
        return result;
    }

    @PostMapping("/import")
    @ApiOperation("导入商品信息")
    @RateLimiter(count = 1, timeunit = TimeUnit.SECONDS, timeout = 30)
    @LogRecordAnno(value = LogEnum.IMPORT_GOODS_EXCEL, content = "#excelGoods.getOriginalFilename()")
    public Result exportGoods(@RequestParam("excelGoods") MultipartFile excelGoods) throws Exception {
        if (excelGoods == null || excelGoods.isEmpty()) {
            return Result.fail(ResultCodeEnum.EXCEL_PATTERN_ERROR);
        }
        // 校验 excel 的上传类型
        String originalFilename = excelGoods.getOriginalFilename();
        if (!originalFilename.endsWith(EXCEL_FILE1) && !originalFilename.endsWith(EXCEL_FILE2)) {
            return Result.fail(ResultCodeEnum.EXCEL_PATTERN_ERROR);
        }
        // 校验文件上传大小
        if (isInValidFileSize(excelGoods, EXCEL_MAX_SIZE, false)) {
            return Result.fail(ResultCodeEnum.EXCEL_MAX_SIZE_ERROR);
        }
        String renameFileName = renameFileName(originalFilename);
        log.info("当前线程：{}", Thread.currentThread().getName());
        try {
            // 校验表头
            EasyExcel.read(excelGoods.getInputStream(), Goods.class, new CheckHeadListener()).sheet().doRead();
            // 文件的状态为导入中
            UploadFile uploadFile = initUploadFileRecord(renameFileName);
            // 保存文件上传记录
            uploadFileService.save(uploadFile);
            // 异步处理
            HANDLE_EXCEL_IMPORT_POOL.submit(() -> {
                try {
                    EasyExcel.read(excelGoods.getInputStream(), Goods.class,
                                    new UploadGoodsListener(goodsService, uploadFileService, uploadFile))
                            .sheet().doRead();
                } catch (IOException e) {
                    log.error("读取 excel 异常：{}", ExceptionUtils.getStackTrace(e));
                }
            });
        } catch (Exception e) {
            if (e instanceof ShopCustomException) {
                return Result.fail(ResultCodeEnum.EXCEL_HEAD_ERROR);
            }
            throw e;
        }
        return Result.ok();
    }


    @DeleteMapping("/delete/{goodsId}")
    @ApiOperation("根据 id 删除商品")
    @LogRecordAnno(LogEnum.UPDATE_OR_DELETE_GOODS)
    public Result deleteById(@PathVariable("goodsId") Long goodsId) {
        if (goodsId == null) {
            return Result.fail();
        }
        return goodsService.deleteById(goodsId);
    }

    private UploadFile initUploadFileRecord(String renameFileName) {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setId(SnowFlakeUtils.nextId());
        uploadFile.setAdminId(AdminHolder.getAdmin().getId());
        uploadFile.setUploadTime(new Date());
        uploadFile.setFileName(renameFileName);
        uploadFile.setStatus(0);
        return uploadFile;
    }

    private String getGoodsImgSavePath(String renameFileName) {
        String goodsImgPathName = GOODS_IMAGES_DIR + renameFileName;
        log.info("商品图片保存路径：{}", goodsImgPathName);
        return goodsImgPathName;
    }

    private void deleteFileIfSaveOrUpdateFail(File file, Result result) {
        if (file != null && result.getCode() < 0) {
            // 保存失败，删除图片
            file.delete();
        }
    }

    /**
     * 文件重命名
     */
    private String renameFileName(String filename) {
        int mid = filename.lastIndexOf(".");
        return filename.substring(0, mid) + "_" + System.currentTimeMillis() + filename.substring(mid);
    }

    private boolean isInValidImgType(MultipartFile goodsImg) {
        String contentType = goodsImg.getContentType();
        if (contentType == null) {
            return true;
        }
        return !contentType.contains(IMAGE_SUFFIX);
    }

    private boolean isInValidFileSize(MultipartFile file, long fileSize, boolean equals) {
        if (equals) {
            return file.getSize() >= fileSize;
        } else {
            return file.getSize() > fileSize;
        }
    }

    private File saveGoodsImage(MultipartFile goodsImg, String renameFileName) throws IOException {
        assert goodsImg.getOriginalFilename() != null;
        // 文件夹不存在就创建
        File goodsImgDir = new File(REAL_GOODS_IMAGES_PATH);
        if (!goodsImgDir.exists()) {
            goodsImgDir.mkdir();
        }
        File goodsImgFile = new File(goodsImgDir, renameFileName);
        goodsImg.transferTo(goodsImgFile.getCanonicalFile());
        return goodsImgFile;
    }
}
