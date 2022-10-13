package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminForgetPasswordDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminForgetPasswordDTO
 * @date 2022/9/14 19:18
 */
@Data
@ApiModel("修改密码表单")
public class AdminForgetPasswordDTO implements Serializable {
    @ApiModelProperty("邮箱")
    @NotBlank
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "请输入正确的邮件账号")
    private String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码错误")
    private String checkCode;

    @ApiModelProperty("新密码")
    @NotNull
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$", message = "密码由8-20位的数字，大小写字母、特殊符号3种以上组成")
    private String newPassword;

    @ApiModelProperty("确认新密码")
    @NotNull
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$", message = "密码由8-20位的数字，大小写字母、特殊符号3种以上组成")
    private String confirmNewPassword;
}
