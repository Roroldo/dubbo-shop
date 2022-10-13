package com.topband.shop.consumer.controller;

import cn.hutool.core.util.StrUtil;
import com.topband.shop.api.AdminService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.config.CustomValidatedGroup;
import com.topband.shop.consumer.anno.limit.RateLimiter;
import com.topband.shop.consumer.anno.root.IsAdmin;
import com.topband.shop.consumer.service.MqEmailSendService;
import com.topband.shop.dto.*;
import com.topband.shop.entity.Admin;
import com.topband.shop.utils.AdminHolder;
import com.topband.shop.utils.RegexUtils;
import com.topband.shop.view.AdminVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.BusinessConstants.CHECK_CODE_COOKIE_NAME;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminController
 * @packageName: com.topband.shop.consumer.controller
 * @description: AdminController
 * @date 2022/9/12 15:22
 */
@Slf4j
@RestController
@RequestMapping("/web/admin")
@Api(tags = "管理员接口")
public class AdminController {
    @DubboReference
    private AdminService adminService;

    @Resource
    private MqEmailSendService mqEmailSendService;

    @PostMapping("/login")
    @ApiOperation("登录接口")
    public Result login(@Validated(CustomValidatedGroup.Crud.Query.class) @RequestBody AdminLoginDTO adminLoginDTO,
                        @CookieValue(CHECK_CODE_COOKIE_NAME) String checkCodeCookieValue) {
        if (StrUtil.isBlank(checkCodeCookieValue)) {
            return Result.fail(ResultCodeEnum.LOGIN_CHECK_CODE_ERROR);
        }
        return adminService.login(adminLoginDTO, checkCodeCookieValue);
    }

    @PostMapping("/exit")
    @ApiOperation("退出接口")
    public Result exit(@RequestHeader("Authorization") String token) {
        if (StrUtil.isBlank(token)) {
            return Result.fail();
        }
        return adminService.exit(token);
    }

    @PostMapping("/password/sendEmail")
    @ApiOperation("发送验证码邮件，给重置密码接口使用")
    @RateLimiter(key = "#email", count = 5, timeout = 1, timeunit = TimeUnit.HOURS)
    public Result sendEmailToFindPassword(String email) {
        if (RegexUtils.isEmailInvalid(email)) {
            return Result.fail(ResultCodeEnum.EMAIL_PATTERN_ERROR);
        }
        if (!adminService.existByEmail(email)) {
            return Result.fail(ResultCodeEnum.EMAIL_NOT_EXISTS);
        }
        // 校验用户是否被冻结，冻结用户就没必要发邮件了
        if (adminService.isFreeze(email)) {
            return Result.fail(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        return mqEmailSendService.sendEmail(email);
    }

    @PostMapping("/email/sendEmail")
    @ApiOperation("发送验证码，给修改邮件接口使用")
    @RateLimiter(key = "#email", count = 5, timeout = 1, timeunit = TimeUnit.HOURS)
    public Result sendEmailToUpdateEmail(String email) {
        if (RegexUtils.isEmailInvalid(email)) {
            return Result.fail(ResultCodeEnum.EMAIL_PATTERN_ERROR);
        }
        // 自己改自己
        String loginEmail = AdminHolder.getAdmin().getEmail();
        if (loginEmail.equals(email)) {
            return Result.fail(ResultCodeEnum.EMAIL_SEND_SELF);
        }
        Admin admin = new Admin(AdminHolder.getAdmin().getId(), email);
        if (adminService.isEmailUsedByOther(admin)) {
            return Result.fail(ResultCodeEnum.REGISTER_EMAIL_ERROR);
        }
        return mqEmailSendService.sendEmail(email);
    }

    @PostMapping("/verify/email")
    @ApiOperation("校验邮箱验证码接口")
    public Result verifyEmail(@RequestBody @Validated AdminVerifyDTO adminVerifyDTO) {
        return adminService.verifyEmail(adminVerifyDTO);
    }

    @PostMapping("/resetPassword")
    @ApiOperation("忘记密码接口")
    public Result resetPasswordByCheckCode(@RequestBody @Validated AdminForgetPasswordDTO adminForgetPasswordDTO) {
        // 校验密码
        if (!StrUtil.equals(adminForgetPasswordDTO.getNewPassword(), adminForgetPasswordDTO.getConfirmNewPassword())) {
            return Result.fail(ResultCodeEnum.PASSWORD_NOT_EQUAL);
        }
        return adminService.resetPasswordByEmailAndCheckCode(adminForgetPasswordDTO);
    }

    @PutMapping("/updatePassword")
    @ApiOperation("修改密码接口")
    public Result updatePassword(@RequestBody @Validated AdminUpdatePasswordDTO adminUpdatePasswordDTO) {
        Long adminId = AdminHolder.getAdmin().getId();
        adminUpdatePasswordDTO.setAdminId(adminId);
        return adminService.updatePasswordById(adminUpdatePasswordDTO);
    }

    @GetMapping("/info")
    @ApiOperation("回显登录用户信息")
    public Result info() {
        return Result.ok(AdminHolder.getAdmin());
    }

    @PutMapping("/updateName")
    @ApiOperation("更新用户名")
    public Result updateAdminName(@RequestBody @Validated(CustomValidatedGroup.Crud.Update.class) AdminVO adminVO) {
        Long adminId = AdminHolder.getAdmin().getId();
        adminVO.setId(adminId);
        return adminService.updateAdminNameByAdminId(adminVO);
    }

    @PutMapping("/updateEmail")
    @ApiOperation("更新邮件接口")
    public Result updateEmail(@RequestBody @Validated(CustomValidatedGroup.Crud.Update.class) AdminVerifyDTO adminVerifyDTO) {
        Long adminId = AdminHolder.getAdmin().getId();
        adminVerifyDTO.setId(adminId);
        return adminService.updateEmailByPasswordAndCheckCode(adminVerifyDTO);
    }

    @PostMapping("/list")
    @ApiOperation("查询管理员列表接口")
    public Result list(@RequestBody AdminQueryDTO adminQueryDTO) {
        return Result.ok(adminService.list(adminQueryDTO));
    }

    @PostMapping("/add")
    @ApiOperation("添加管理员")
    public Result add(@RequestBody @Validated(CustomValidatedGroup.Crud.Create.class)
                                  AdminLoginDTO adminLoginDTO) {
        return adminService.add(adminLoginDTO);
    }

    @DeleteMapping("/delete/{adminId}")
    @ApiOperation("删除管理员")
    @IsAdmin
    public Result delete(@PathVariable("adminId") Long adminId) {
        if (adminId == null) {
            return Result.fail();
        }
        return adminService.deleteById(adminId);
    }

    @PutMapping("/updateAdmin")
    @ApiOperation("修改管理员")
    @IsAdmin
    public Result update(@RequestBody @Validated AdminEditDTO adminEditDTO) {
        adminService.updateById(adminEditDTO);
        return Result.ok();
    }

    @GetMapping("/{adminId}")
    @ApiOperation("根据管理员 id 查找信息")
    public Result findById(@PathVariable("adminId") Long adminId) {
        if (adminId == null) {
            return Result.fail();
        }
        return Result.ok(adminService.selectById(adminId));
    }
}
