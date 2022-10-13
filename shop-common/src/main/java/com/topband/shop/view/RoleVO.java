package com.topband.shop.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleVO
 * @packageName: com.topband.shop.view
 * @description: RoleVO
 * @date 2022/9/12 20:08
 */
@Data
@ApiModel("角色视图实体类")
public class RoleVO implements Serializable {
    @ApiModelProperty("角色 id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("创建角色管理员名")
    private String createAdminName;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
