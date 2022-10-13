package com.topband.shop.consumer.anno.log;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecordAnno {
    /**
     * 日志类型
     */
    LogEnum value();

    /**
     * 记录的日志值
     */
    String content() default "";
}
