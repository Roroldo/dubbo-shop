package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherDTO
 * @packageName: com.topband.shop.dto
 * @description: VoucherDTO
 * @date 2022/9/13 10:40
 */
@Data
@ApiModel("优惠卷")
public class VoucherDTO implements Serializable {
    @ApiModelProperty("优惠卷名称")
    @NotBlank(message = "请输入 1-50 个字符")
    @Length(min = 1, max = 50, message = "请输入 1-50 个字符")
    private String name;

    @ApiModelProperty("过期时间")
    // 时间必须是将来的日期
    @Future(message = "优惠卷时间不能为过期")
    @NotNull
    private Date expireTime;

    @ApiModelProperty("数量")
    @NotNull(message = "优惠卷数量需要在 1 - 999 之间" )
    @Range(min = 1, max = 999, message = "优惠卷数量需要在 1 - 999 之间")
    private Integer count;

    @ApiModelProperty("优惠价格")
    @NotNull
    @Digits(integer = 4, fraction = 2, message = "优惠卷价格需要在 0 - 1000 之间")
    @DecimalMin(value = "0.01", message = "优惠卷价格需要在 0 - 1000 之间")
    @DecimalMax(value = "1000", message = "优惠卷价格需要在 0 - 1000 之间")
    private BigDecimal voucherPrice;
}
