package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminEditDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminEditDTO
 * @date 2022/9/19 8:56
 */
@Data
@ApiModel("编辑成员表单")
public class AdminEditDTO implements Serializable {
    @ApiModelProperty("管理员 id")
    @NotNull
    private Long adminId;

    @ApiModelProperty("角色 id")
    @NotNull
    private Long roleId;

    @ApiModelProperty("状态，0 为正常，1 为冻结")
    @NotNull
    private Integer status;

    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateTime;
}
