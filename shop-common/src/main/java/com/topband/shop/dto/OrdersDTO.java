package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: OrderDTO
 * @packageName: com.topband.shop.dto
 * @description: OrderDTO
 * @date 2022/9/13 17:20
 */
@Data
@ApiModel("订单 dto")
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO implements Serializable {
    @ApiModelProperty(value = "当前用户 id", hidden = true)
    private Long userId;

    @ApiModelProperty("商品 id")
    private Long goodsId;

    @ApiModelProperty(value = "订单流水号", hidden = true)
    private String ordersSerial;
}
