package com.topband.shop.consumer.anno.limit;

import com.topband.shop.constants.RedisConstants;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: RateLimiter
 * @packageName: com.topband.shop.consumer.limit
 * @description: 限流注解
 * @date: 2022-08-23 14:31
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {
    /**
     * 缓存空间名称
     */
    @AliasFor("cacheNames")
    String value() default RedisConstants.RATE_LIMITER_KEY_PREFIX;

    @AliasFor("value")
    String cacheNames() default RedisConstants.RATE_LIMITER_KEY_PREFIX;

    /**
     * 限流 key，支持 el 表达式
     */
    String key() default "";

    /**
     * 限流时间，单位毫秒
     */
    long timeout();

    /**
     * 限流时间单元
     */
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    /**
     * 限流次数
     */
    int count() default 60;

    /**
     * 限流策略
     */
    LimitStrategy limitStrategy() default LimitStrategy.DEFAULT;
}