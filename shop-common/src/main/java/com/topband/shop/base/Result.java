package com.topband.shop.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: Result
 * @packageName: com.roroldo.jdbc.demo
 * @description: Result<T>
 * @date 2022/8/17 21:58
 */
@Data
@ApiModel("响应结果类")
public class Result<T> implements Serializable {
    /**
     * 返回状态码
     */
    @ApiModelProperty("响应状态码")
    private int code;

    /**
     * 返回操作信息
     */
    @ApiModelProperty("操作信息")
    private String message;

    /**
     * 返回数据
     */
    @ApiModelProperty("响应数据")
    private T data;

    /**
     * 时间戳
     */
    @ApiModelProperty("时间戳")
    private Long timestamp = System.currentTimeMillis();


    public static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(data);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    public static <T> Result<T> build(int status, String message) {
        Result<T> result = build(null);
        result.setCode(status);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> build() {
        return build(null);
    }

    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(String message) {
        return fail(-1, message);
    }

    public static <T> Result<T> fail(int code, String message) {
        return build(code, message);
    }

    public static <T> Result<T> fail(ResultCodeEnum resultCodeEnum) {
        return build(resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }
}
