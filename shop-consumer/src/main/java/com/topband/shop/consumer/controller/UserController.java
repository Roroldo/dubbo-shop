package com.topband.shop.consumer.controller;

import cn.hutool.core.util.StrUtil;
import com.topband.shop.api.UserActiveService;
import com.topband.shop.api.UserService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.dto.UserLoginRegisterDTO;
import com.topband.shop.utils.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.topband.shop.constants.BusinessConstants.DAY_MAX;
import static com.topband.shop.constants.BusinessConstants.DAY_MIN;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserController
 * @packageName: com.topband.shop.consumer.controller
 * @description: UserController
 * @date 2022/9/8 14:33
 */
@Slf4j
@RestController
@RequestMapping("/client/user")
@Api(tags = "用户接口")
public class UserController {
    @DubboReference
    private UserService userService;

    @DubboReference
    private UserActiveService userActiveService;


    @PostMapping("/register")
    @ApiOperation("注册用户")
    public Result register(@Validated @RequestBody UserLoginRegisterDTO userRegisterDTO) {
        if (RegexUtils.isPasswordInvalid(userRegisterDTO.getPassword())) {
            return Result.fail(ResultCodeEnum.PASSWORD_PATTERN_ERROR);
        }
        boolean flag = userService.register(userRegisterDTO);
        if (!flag) {
            return Result.fail(ResultCodeEnum.REGISTER_EMAIL_ERROR);
        }
        return Result.ok();
    }

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public Result login(@Validated @RequestBody UserLoginRegisterDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    @GetMapping("/active/{day}")
    @ApiOperation("每日用户活跃次数接口")
    public Result active(@PathVariable int day) {
        if (day < DAY_MIN || day > DAY_MAX) {
            return Result.fail(ResultCodeEnum.DAY_ERROR);
        }
        return Result.ok(userActiveService.countActiveUserByDate(day));
    }

    @GetMapping("/new/{day}")
    @ApiOperation("每日用户新增接口")
    public Result incr(@PathVariable int day) {
        if (day < DAY_MIN || day > DAY_MAX) {
            return Result.fail(ResultCodeEnum.DAY_ERROR);
        }
        return Result.ok(userActiveService.countNewUserByDate(day));
    }

    @PostMapping("/exit")
    @ApiOperation("退出接口")
    public Result exit(@RequestHeader("Authorization") String token) {
        if (StrUtil.isBlank(token)) {
            return Result.fail();
        }
        return userService.exit(token);
    }
}
