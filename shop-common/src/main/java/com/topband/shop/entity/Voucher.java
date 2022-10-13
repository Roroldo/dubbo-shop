package com.topband.shop.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Voucher
 * @packageName: com.topband.shop.entity
 * @description: Voucher
 * @date 2022/9/11 16:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("优惠卷实体类")
public class Voucher extends BaseEntity {
    @ApiModelProperty("优惠卷 id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("过期时间")
    private Date expireTime;

    @ApiModelProperty("数量")
    private Integer count;

    @ApiModelProperty("优惠价格")
    private BigDecimal voucherPrice;
}
