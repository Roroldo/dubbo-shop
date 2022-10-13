package com.topband.shop.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherVO
 * @packageName: com.topband.shop.view
 * @description: VoucherVO
 * @date 2022/9/11 17:17
 */
@Data
public class VoucherVO implements Serializable {
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
