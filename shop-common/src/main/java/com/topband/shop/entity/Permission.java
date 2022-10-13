package com.topband.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Permission
 * @packageName: com.topband.shop.entity
 * @description: Permission
 * @date 2022/9/19 14:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends BaseEntity {
    private Long id;
    private String url;
    private String name;
    private Integer order;
    private Integer type;
    private Long parentId;
}
