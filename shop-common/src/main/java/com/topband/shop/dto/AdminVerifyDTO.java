package com.topband.shop.dto;

import com.topband.shop.config.CustomValidatedGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminVerifyDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminVerifyDTO
 * @date 2022/9/14 16:57
 */
@Data
@ApiModel("忘记密码验证表单类")
@NoArgsConstructor
public class AdminVerifyDTO implements Serializable {
    @ApiModelProperty(value = "管理员 id")
    private Long id;

    @ApiModelProperty("邮箱")
    @NotNull(message = "请输入正确的邮箱")
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "请输入正确的邮件账号")
    @Length(min = 1, max = 100, message = "请输入正确的邮箱")
    private String email;

    @ApiModelProperty("验证码")
    @NotBlank(message = "验证码错误")
    private String checkCode;

    @ApiModelProperty("密码")
    @NotBlank(message = "密码错误", groups = CustomValidatedGroup.Crud.Update.class)
    private String password;

    public AdminVerifyDTO(String email, String checkCode) {
        this.email = email;
        this.checkCode = checkCode;
    }
}
