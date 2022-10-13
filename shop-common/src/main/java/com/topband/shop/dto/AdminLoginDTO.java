package com.topband.shop.dto;

import com.topband.shop.config.CustomValidatedGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminLoginDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminLoginDTO
 * @date 2022/9/12 17:12
 */
@Data
@ApiModel("管理员登录表单")
public class AdminLoginDTO implements Serializable {
    @ApiModelProperty("管理员邮箱")
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$",
            message = "请输入正确的邮件账号", groups = {CustomValidatedGroup.Crud.Create.class,
            CustomValidatedGroup.Crud.Query.class})
    private String email;

    @ApiModelProperty("管理员密码")
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$",
            message = "密码由8-20位的数字，大小写字母、特殊符号3种以上组成", groups = CustomValidatedGroup.Crud.Create.class)
    private String password;

    @ApiModelProperty("验证码")
    @NotBlank(groups = CustomValidatedGroup.Crud.Query.class)
    private String checkCode;

    @ApiModelProperty("角色 id")
    @NotNull(groups = CustomValidatedGroup.Crud.Create.class, message = "角色 id 不能为 null")
    private Long roleId;

    @ApiModelProperty("用户名称")
    @NotBlank(groups = CustomValidatedGroup.Crud.Create.class, message = "用户名称不能为 null")
    @Length(min = 1, max = 50, groups = CustomValidatedGroup.Crud.Create.class, message = "用户名在 1-50 字符")
    private String name;
}
