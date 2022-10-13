package com.topband.shop.consumer.anno.limit;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LimitType
 * @packageName: com.roroldo.redis-limiter.emum
 * @description: 限流策略枚举类
 * @date 2022/8/23 14:28
 */
public enum LimitStrategy {
    /**
     * 默认策略全局限流：针对当前接口的全局性限流
     */
    DEFAULT,
    /**
     * 根据请求者IP进行限流
     */
    IP,
}
