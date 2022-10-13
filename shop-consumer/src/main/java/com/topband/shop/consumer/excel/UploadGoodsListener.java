package com.topband.shop.consumer.excel;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.topband.shop.api.GoodsService;
import com.topband.shop.api.UploadFileService;
import com.topband.shop.entity.Goods;
import com.topband.shop.entity.UploadFile;
import com.topband.shop.view.GoodsVO;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.topband.shop.constants.BusinessConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UploadGoodsListener
 * @packageName: com.topband.shop.consumer.excel
 * @description: UploadGoodsListener: 处理上传数据
 * @date 2022/9/16 19:28
 */
@Slf4j
@NoArgsConstructor
public class UploadGoodsListener extends AbstractUploadsListener {
    private static final int BATCH_COUNT = 100;
    private static final int BATCH_WRITE_COUNT = 500;

    private Set<Goods> cacheGoodsSet = new HashSet<>(BATCH_COUNT);
    private List<Goods> writeGoodsList = new ArrayList<>(BATCH_WRITE_COUNT);

    private ExcelWriter excelWriter;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private GoodsService goodsService;

    private UploadFileService uploadFileService;

    private UploadFile uploadFile;

    public UploadGoodsListener(GoodsService goodsService, UploadFileService uploadFileService, UploadFile uploadFile) {
        this.goodsService = goodsService;
        this.uploadFileService = uploadFileService;
        this.uploadFile = uploadFile;
        File goodsExcelDir = new File(REAL_GOODS_EXCEL_PATH);
        if (!goodsExcelDir.exists()) {
            goodsExcelDir.mkdir();
        }
        String fileName = REAL_GOODS_EXCEL_PATH + uploadFile.getFileName();
        log.info("批量导入 excel 路径：{}", fileName);
        excelWriter = EasyExcel.write(fileName).build();
    }


    @Override
    public void invoke(Goods goods, AnalysisContext analysisContext) {
        log.debug("解析出了一条数据：{}", JSONUtil.toJsonStr(goods));
        // 校验商品
        if (isInvalidGoods(goods) || cacheGoodsSet.contains(goods)) {
            // 记录下来这是导入失败的
            goods.setExtra("导入失败");
        } else {
            goods.setCreateTime(new Date());
            goods.setExtra(sdf.format(goods.getCreateTime()));
            cacheGoodsSet.add(goods);
            if (cacheGoodsSet.size() >= BATCH_COUNT) {
                saveGoodsList();
                cacheGoodsSet.clear();
            }
        }
        writeGoodsList.add(goods);
        if (writeGoodsList.size() >= BATCH_WRITE_COUNT) {
            writeDataToExcel();
        }
    }

    /**
     * 写入 excel
     */
    private void writeDataToExcel() {
        List<GoodsVO> goodsVOList = BeanUtil.copyToList(writeGoodsList, GoodsVO.class);
        WriteSheet originalWriteSheet = EasyExcel.writerSheet(0, EXPORT_GOODS_EXCEL_ORIGINAL_SHEET_NAME)
                .head(GoodsVO.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
        WriteSheet feedbackWriteSheet = EasyExcel.writerSheet(1, EXPORT_GOODS_EXCEL_FEED_BACK_SHEET_NAME)
                .head(Goods.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()).build();
        excelWriter.write(goodsVOList, originalWriteSheet);
        excelWriter.write(writeGoodsList, feedbackWriteSheet);
        writeGoodsList.clear();
    }

    private boolean isInvalidGoods(Goods goods) {
        String goodsName = goods.getName();
        Integer total = goods.getTotal();
        BigDecimal goodsPrice = goods.getGoodsPrice();
        return StrUtil.isBlank(goodsName) || goodsService.repeatName(goodsName)
                || isInvalidPriceOrTotal(goodsPrice, total);
    }

    private boolean isInvalidPriceOrTotal(BigDecimal price, Integer total) {
        if (price == null || total == null) {
            return true;
        }
        if (total < 0 || total >= 1000) {
            return true;
        }
        BigDecimal min = new BigDecimal("0");
        BigDecimal max = new BigDecimal("1000");
        return price.compareTo(min) < 1 || price.compareTo(max) > -1;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        writeDataToExcel();
        saveGoodsList();
        log.info("所有数据解析完成，修改文件状态为导入完成");
        // 修改文件为导入成功
        uploadFile.setStatus(1);
        uploadFileService.updateById(uploadFile);
        excelWriter.close();
    }

    private void saveGoodsList() {
        log.info("{} 条数据，开始存储数据库！", cacheGoodsSet.size());
        goodsService.saveBatch(cacheGoodsSet);
        log.info("存储数据库成功！");
    }
}
