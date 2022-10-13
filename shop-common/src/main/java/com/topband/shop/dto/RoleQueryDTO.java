package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleQueryDTO
 * @packageName: com.topband.shop.dto
 * @description: RoleQueryDTO
 * @date 2022/9/19 11:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("角色查询表单")
public class RoleQueryDTO extends PageQueryDTO {
    @ApiModelProperty("角色名称")
    private String roleName;
}
