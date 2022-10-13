package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Role
 * @packageName: com.topband.shop.entity
 * @description: Role
 * @date 2022/9/12 20:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("角色实体类")
public class Role extends BaseEntity {
    private Long id;

    private String name;

    private Long createAdminId;
}
