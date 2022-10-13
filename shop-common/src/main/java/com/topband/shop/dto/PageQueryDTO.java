package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PageQueryDTO
 * @packageName: com.topband.shop.dto
 * @description: PageQueryDTO
 * @date 2022/9/15 16:53
 */
@ApiModel("分页时间查询")
@Data
public class PageQueryDTO implements Serializable {
    @ApiModelProperty(value = "开始日期", example = "2018-10-01 12:18:00")
    protected Date start;

    @ApiModelProperty(value = "结束日期", example = "2018-10-01 12:18:00")
    protected Date end;

    @ApiModelProperty("当前页")
    protected Integer currentPage;

    @ApiModelProperty("每页大小")
    protected Integer pageSize;

    @ApiModelProperty(value = "起始索引", hidden = true)
    private Integer startIndex;

    @ApiModelProperty(value = "结束索引", hidden = true)
    private Integer endIndex;
}
