package com.topband.shop.consumer.controller;

import com.topband.shop.api.VoucherService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.dto.VoucherDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherController
 * @packageName: com.topband.shop.consumer.controller
 * @description: VoucherController
 * @date 2022/9/11 16:58
 */
@Slf4j
@RestController
@RequestMapping("/voucher")
@Api(tags = "优惠卷接口")
public class VoucherController {
    @DubboReference
    private VoucherService voucherService;

    @PostMapping("/list")
    @ApiOperation("web 端分页条件查询优惠卷")
    public Result list(@RequestBody PageQueryDTO pageQueryDTO) {
        return Result.ok(voucherService.list(pageQueryDTO, true));
    }

    @PostMapping("/client/list")
    @ApiOperation("c 端分页条件查询优惠卷")
    public Result listForClient(@RequestBody PageQueryDTO pageQueryDTO) {
        return Result.ok(voucherService.list(pageQueryDTO, false));
    }

    @PostMapping("/add")
    @ApiOperation("添加优惠卷")
    public Result add(@RequestBody @Validated VoucherDTO voucherDTO) {
        boolean flag = voucherService.add(voucherDTO);
        if (!flag) {
            return Result.fail(ResultCodeEnum.VOUCHER_ADD_ERROR);
        }
        return Result.ok();
    }
}
