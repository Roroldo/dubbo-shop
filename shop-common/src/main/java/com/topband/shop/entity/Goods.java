package com.topband.shop.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Goods
 * @packageName: com.topband.shop.entity
 * @description: Goods
 * @date 2022/9/12 13:25
 */
@Data
@ApiModel("商品实体类")
public class Goods extends BaseEntity {
    @ApiModelProperty("商品 id")
    @ExcelIgnore
    private Long id;

    @ApiModelProperty("商品品类")
    @ExcelIgnore
    private String category;

    @ApiModelProperty("图片 url")
    @ExcelIgnore
    private String imageUrl;

    @ApiModelProperty("商品名称")
    @ExcelProperty(value = "商品名称", index = 0)
    private String name;

    @ApiModelProperty("商品库存")
    @ExcelProperty(value = "商品库存", index = 1)
    private Integer total;

    @ApiModelProperty("商品价格")
    @ContentStyle(dataFormat = 2)
    @ExcelProperty(value = "商品价格", index = 2)
    private BigDecimal goodsPrice;

    @ApiModelProperty(hidden = true)
    @ExcelProperty(value = "导入时间", index = 3)
    private String extra;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Goods goods = (Goods) o;
        return Objects.equals(id, goods.id) && name.equals(goods.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
