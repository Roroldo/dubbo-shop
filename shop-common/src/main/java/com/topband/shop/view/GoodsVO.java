package com.topband.shop.view;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.topband.shop.config.CustomValidatedGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsVO
 * @packageName: com.topband.shop.view
 * @description: GoodsVO
 * @date 2022/9/12 13:38
 */
@Data
@ApiModel("商品类")
public class GoodsVO implements Serializable {
    @ApiModelProperty(value = "商品 id, 新增商品不用填写")
    @NotNull(message = "更新商品 id 不能为 null", groups = {
            CustomValidatedGroup.Crud.Update.class,
    })
    @ExcelIgnore
    private Long id;

    @ApiModelProperty("商品品类")
    @ExcelIgnore
    private String category;

    @ApiModelProperty(value = "商品图片", hidden = true)
    @ExcelIgnore
    private String imageUrl;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "请输入 1-50 个字符", groups = {
            CustomValidatedGroup.Crud.Create.class,
            CustomValidatedGroup.Crud.Update.class,
    })
    @Length(min = 1, max = 50, message = "请输入 1-50 个字符", groups = CustomValidatedGroup.Crud.Create.class)
    @ExcelProperty(value = "商品名称", index = 0)
    private String name;

    @ApiModelProperty("商品库存")
    @ExcelProperty(value = "商品库存", index = 1)
    @NotNull(message = "商品库存数量需要在 1 - 999 之间", groups = CustomValidatedGroup.Crud.Create.class)
    @Range(min = 1, max = 999, message = "商品库存数量需要在 1 - 999 之间", groups = CustomValidatedGroup.Crud.Create.class)
    private Integer total;

    @ApiModelProperty("商品价格")
    @Digits(integer = 3, fraction = 2, message = "商品价格需要在 0 - 1000 之间，不含 1000", groups = CustomValidatedGroup.Crud.Create.class)
    @DecimalMin(value = "0.01", message = "商品价格需要在 0 - 1000 之间，不含 1000", groups = CustomValidatedGroup.Crud.Create.class)
    @DecimalMax(value = "999.99", message = "商品价格需要在 0 - 1000 之间，不含 1000", groups = CustomValidatedGroup.Crud.Create.class)
    @NotNull(message = "商品价格需要在 0 - 1000 之间，不含 1000", groups = CustomValidatedGroup.Crud.Create.class)
    @ExcelProperty(value = "商品价格", index = 2)
    private BigDecimal goodsPrice;
}
