package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsDTo
 * @packageName: com.topband.shop.dto
 * @description: GoodsDTO
 * @date 2022/9/12 13:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("商品查询条件实体类")
public class GoodsQueryDTO extends PageQueryDTO {
    @ApiModelProperty("商品名称")
    private String name;
}
