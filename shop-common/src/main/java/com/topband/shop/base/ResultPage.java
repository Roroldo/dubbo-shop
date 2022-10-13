package com.topband.shop.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ResultPage
 * @packageName: com.topband.shop.base
 * @description: 带分页的结果集
 * @date 2022/8/24 16:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPage<T> implements Serializable {
    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("每页的数据")
    private T rows;

    public static <T> ResultPage<T> build(long total, T data) {
        return new ResultPage<>(total, data);
    }
}
