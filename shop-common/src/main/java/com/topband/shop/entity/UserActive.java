package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserActive
 * @packageName: com.topband.shop.entity
 * @description: 用户活跃对象
 * @date 2022/9/9 15:35
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户活跃实体类")
@Data
@NoArgsConstructor
public class UserActive extends BaseEntity {
    @ApiModelProperty("主键 id")
    private Long id;

    @ApiModelProperty("用户 id")
    private Long userId;

    @ApiModelProperty("活跃时间")
    private Date activateTime;

    public UserActive(Long userId, Date activateTime) {
        this.userId = userId;
        this.activateTime = activateTime;
    }
}
