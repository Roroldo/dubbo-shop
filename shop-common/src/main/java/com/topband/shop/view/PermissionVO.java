package com.topband.shop.view;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PermissionVO
 * @packageName: com.topband.shop.view
 * @description: PermissionVO
 * @date 2022/9/19 15:11
 */
@Data
public class PermissionVO implements Serializable {
    private Long id;
    private String name;
    private Long parentId;
    private Boolean selected;
    private String url;
    private Integer type;
    private Integer order;
    private List<PermissionVO> children;
}
