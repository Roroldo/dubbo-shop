package com.topband.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminRole
 * @packageName: com.topband.shop.entity
 * @description: AdminRole
 * @date 2022/9/18 20:26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRole extends BaseEntity {
    private Long id;
    private Long adminId;
    private Long roleId;
}
