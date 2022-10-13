package com.topband.shop.consumer.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.topband.shop.api.UploadFileService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.dto.PageQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import static com.topband.shop.constants.BusinessConstants.REAL_GOODS_EXCEL_PATH;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UploadFileController
 * @packageName: com.topband.shop.consumer.controller
 * @description: UploadFileController
 * @date 2022/9/17 17:49
 */
@Api(tags = "商品导入记录接口")
@Slf4j
@RestController
@RequestMapping("/shop-records")
public class UploadFileController {
    @DubboReference
    private UploadFileService uploadFileService;

    @PostMapping("/list")
    @ApiOperation("查询上传日志")
    public Result list(@RequestBody PageQueryDTO pageQueryDTO) {
        return Result.ok(uploadFileService.list(pageQueryDTO));
    }

    @GetMapping("/download")
    @ApiOperation("下载导入反馈 excel")
    public void download(String filename, HttpServletResponse response) throws IOException {
        File file = new File(REAL_GOODS_EXCEL_PATH + filename);
        if (StrUtil.isBlank(filename) || !file.exists()) {
            handlerFileDownloadError(response);
            return;
        }
        String downloadFileName = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");;
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + downloadFileName);
        BufferedInputStream bufferedInputStream = FileUtil.getInputStream(file);
        try {
            IoUtil.copy(bufferedInputStream, response.getOutputStream());
        } catch (IOException e) {
            handlerFileDownloadError(response);
        } finally {
            IoUtil.close(bufferedInputStream);
            IoUtil.close(response.getOutputStream());
        }
    }

    private void handlerFileDownloadError(HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        JSONConfig jsonConfig = JSONConfig.create().setIgnoreNullValue(false);
        response.getWriter().write(JSONUtil.toJsonStr(Result.fail(ResultCodeEnum.FILE_DOWNLOAD_ERROR), jsonConfig));
    }
}
