package com.topband.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LogRecord
 * @packageName: com.topband.shop.entity
 * @description: LogRecord
 * @date 2022/9/17 20:39
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogRecord extends BaseEntity {
    private Long id;
    private Long adminId;
    private String content;
}
