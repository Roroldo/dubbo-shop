package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: User
 * @packageName: com.topband.shop.entity
 * @description: User
 * @date 2022/9/8 14:21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户实体类")
@Data
public class User extends BaseEntity {
    @ApiModelProperty("用户 id")
    private Long id;

    @ApiModelProperty("用户邮箱")
    @NotNull
    @Pattern(regexp = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", message = "请输入正确的邮件账号")
    private String email;

    @NotNull
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$", message = "密码由8-20位的数字，大小写字母、特殊符号3种以上组成")
    @ApiModelProperty("用户密码")
    private String password;
}
