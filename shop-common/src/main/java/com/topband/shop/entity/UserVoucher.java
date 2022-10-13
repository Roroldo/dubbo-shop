package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucher
 * @packageName: com.topband.shop.entity
 * @description: UserVoucher
 * @date 2022/9/13 16:09
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("用户优惠卷关联实体类")
public class UserVoucher extends BaseEntity {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("用户主键")
    private Long userId;

    @ApiModelProperty("优惠卷主键")
    private Long voucherId;
}
