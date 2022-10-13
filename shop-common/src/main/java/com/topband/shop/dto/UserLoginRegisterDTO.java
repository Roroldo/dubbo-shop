package com.topband.shop.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserLoginDTO
 * @packageName: com.topband.shop.view
 * @description: UserLoginRegisterDTO
 * @date 2022/9/9 15:10
 */
@Data
public class UserLoginRegisterDTO implements Serializable {
    @ApiModelProperty("用户邮箱")
    @NotBlank
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "请输入正确的邮件账号")
    private String email;

    @NotBlank
    @ApiModelProperty("用户密码")
    private String password;
}
