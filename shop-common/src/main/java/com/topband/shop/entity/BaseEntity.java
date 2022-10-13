package com.topband.shop.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: BaseEntity
 * @packageName: com.topband.shop.entity
 * @description: BaseEntity
 * @date 2022/9/12 13:26
 */
@Data
@ApiModel("基本实体类")
public class BaseEntity implements Serializable {
    @ApiModelProperty("创建时间")
    @ExcelIgnore
    protected Date createTime;

    @ApiModelProperty("更新时间")
    @ExcelIgnore
    protected Date updateTime;

    @ApiModelProperty("删除标志")
    @ExcelIgnore
    protected Short delFlag;
}
