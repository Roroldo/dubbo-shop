package com.topband.shop.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UploadFile
 * @packageName: com.topband.shop.entity
 * @description: UploadFile
 * @date 2022/9/16 19:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UploadFile extends BaseEntity {
    private Long id;
    private Long adminId;
    private Integer status;
    private String fileName;
    private Date uploadTime;
}
