package com.topband.shop.view;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: OrdersVO
 * @packageName: com.topband.shop.view
 * @description: OrdersVO
 * @date 2022/9/15 17:05
 */
@Data
@ApiModel("订单视图对象")
public class OrdersVO implements Serializable {
    @ApiModelProperty("订单 id")
    @ExcelProperty(value = "订单 id", index = 0)
    private String id;

    @ApiModelProperty("订单金额")
    @ExcelProperty(value = "订单金额", index = 1)
    private BigDecimal orderPrice;

    @ApiModelProperty("用户邮箱")
    @ExcelProperty(value = "用户邮箱", index = 2)
    private String userEmail;

    @ApiModelProperty("优惠卷名称")
    @ExcelProperty(value = "优惠卷名称", index = 3)
    private String voucherName;

    @ApiModelProperty("商品名称")
    @ExcelProperty(value = "商品名称", index = 4)
    private String goodsName;

    @ApiModelProperty("创建时间")
    @ExcelProperty(value = "创建时间", index = 5)
    @ColumnWidth(40)
    private Date createTime;
}
