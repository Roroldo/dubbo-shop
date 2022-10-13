package com.topband.shop.exception;

import com.topband.shop.base.ResultCodeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ShopCustomException
 * @packageName: com.topband.shop.exception
 * @description: ShopCustomException
 * @date 2022/9/9 9:03
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ShopCustomException extends RuntimeException {
    @ApiModelProperty(value = "异常状态码")
    private int code;

    /**
     * 通过状态码和错误消息创建异常对象
     */
    public ShopCustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     */
    public ShopCustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "ShopCustomException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
