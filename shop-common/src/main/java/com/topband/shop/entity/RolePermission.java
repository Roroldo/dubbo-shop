package com.topband.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RolePermission
 * @packageName: com.topband.shop.entity
 * @description: RolePermission
 * @date 2022/9/19 16:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission extends BaseEntity{
    private Long id;
    private Long roleId;
    private Long permissionId;
}
