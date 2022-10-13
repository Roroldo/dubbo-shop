package com.topband.shop.view;

import lombok.Data;

import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserActiveVO
 * @packageName: com.topband.shop.view
 * @description: UserActiveVO
 * @date 2022/9/10 15:35
 */
@Data
public class UserActiveVO implements Serializable {
    private Integer count;
    private String date;
}
