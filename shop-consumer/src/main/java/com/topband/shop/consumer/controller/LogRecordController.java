package com.topband.shop.consumer.controller;

import com.topband.shop.api.LogRecordService;
import com.topband.shop.base.Result;
import com.topband.shop.dto.PageQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LogRecordCOntroller
 * @packageName: com.topband.shop.consumer.controller
 * @description: LogRecordController
 * @date 2022/9/18 17:51
 */
@Slf4j
@RestController
@RequestMapping("/log-record")
@Api(tags = "日志接口")
public class LogRecordController {
    @DubboReference
    private LogRecordService logRecordService;

    @PostMapping("/list")
    @ApiOperation("分页条件查询日志")
    public Result list(@RequestBody PageQueryDTO pageQueryDTO) {
        return Result.ok(logRecordService.list(pageQueryDTO));
    }
}
