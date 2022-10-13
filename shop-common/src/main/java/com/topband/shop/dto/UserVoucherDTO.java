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
 * @className: UserVoucherDTO
 * @packageName: com.topband.shop.dto
 * @description: UserVoucherDTO
 * @date 2022/9/13 14:59
 */
@Data
@ApiModel("抢购优惠卷实体类")
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherDTO implements Serializable {
    @ApiModelProperty("当前用户 id")
    private Long userId;
    @ApiModelProperty("优惠卷 id")
    private Long voucherId;
}
