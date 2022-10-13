package com.topband.shop.consumer.controller;

import com.topband.shop.api.UserVoucherService;
import com.topband.shop.base.Result;
import com.topband.shop.dto.UserVoucherDTO;
import com.topband.shop.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucherController
 * @packageName: com.topband.shop.consumer.controller
 * @description: UserVoucherController
 * @date 2022/9/13 14:55
 */
@RequestMapping("/user-voucher")
@Api(tags = "领取优惠卷接口")
@RestController
public class UserVoucherController {
    @DubboReference
    private UserVoucherService userVoucherService;

    @PostMapping("spike/{voucherId}")
    @ApiOperation("秒杀优惠卷")
    public Result spikeVoucher(@PathVariable("voucherId") Long voucherId) {
        if (voucherId == null || voucherId <= 0) {
            return Result.fail();
        }
        Long userId = UserHolder.getUser().getId();
        UserVoucherDTO userVoucherDTO = new UserVoucherDTO(userId, voucherId);
        return userVoucherService.spikeVoucher(userVoucherDTO);
    }
}
