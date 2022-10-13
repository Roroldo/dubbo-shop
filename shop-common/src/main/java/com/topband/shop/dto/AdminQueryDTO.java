package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminQueryDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminQueryDTO
 * @date 2022/9/16 10:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("查询管理员 dto")
public class AdminQueryDTO extends PageQueryDTO {
    @ApiModelProperty("管理员名")
    private String name;

    @ApiModelProperty("管理员邮箱")
    private String email;
}
