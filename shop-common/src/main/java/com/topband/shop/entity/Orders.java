package com.topband.shop.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Order
 * @packageName: com.topband.shop.entity
 * @description: Orders
 * @date 2022/9/13 17:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Orders extends BaseEntity {
    @ApiModelProperty("订单 id")
    private Long id;

    @ApiModelProperty("用户 id")
    private Long userId;

    @ApiModelProperty("优惠卷 id")
    private Long voucherId;

    @ApiModelProperty("商品 id")
    private Long goodsId;

    @ApiModelProperty("订单金额")
    private BigDecimal orderPrice;
}
