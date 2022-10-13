package com.topband.shop.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LogRecordVO
 * @packageName: com.topband.shop.view
 * @description: LogRecordVO
 * @date 2022/9/18 17:40
 */
@ApiModel("日志记录视图类")
@Data
public class LogRecordVO implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("操作人")
    private String adminName;

    @ApiModelProperty("操作内容")
    private String content;

    @ApiModelProperty("创建日期")
    private Date createTime;
}
