package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Admin
 * @packageName: com.topband.shop.entity
 * @description: Admin
 * @date 2022/9/12 17:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("管理员实体类")
@NoArgsConstructor
public class Admin extends BaseEntity {
    @ApiModelProperty("管理员 id")
    private Long id;

    @ApiModelProperty("管理员名")
    private String name;

    @ApiModelProperty("管理员邮箱")
    private String email;

    @ApiModelProperty("管理员密码")
    private String password;

    @ApiModelProperty("管理员类型，0 为普通管理员，1 为超级管理员")
    private Short type;

    @ApiModelProperty("账号状态，0 为正常，1 为锁定")
    private Short status;

    public Admin(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
