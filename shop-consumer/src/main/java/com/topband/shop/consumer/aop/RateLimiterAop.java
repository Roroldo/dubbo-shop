package com.topband.shop.consumer.aop;

import cn.hutool.core.util.StrUtil;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.consumer.anno.limit.LimitStrategy;
import com.topband.shop.consumer.anno.limit.RateLimiter;
import com.topband.shop.exception.ShopCustomException;
import com.topband.shop.utils.ParseElUtils;
import com.topband.shop.utils.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RateLimiterInterceptor
 * @packageName: com.topband.shop.consumer.config
 * @description: 限流 aop
 * @date 2022/9/15 13:57
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAop implements HandlerInterceptor {
    @Resource
    private RedisScript<Long> limitScript;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 拦截包路径的范围：com.topband.four.controller
     */
    @Pointcut("within(com.topband.shop.consumer.controller.*)")
    private void limitControllerPoint() {
    }

    /**
     * 拦截 com.topband.four.controller 下 有 LimitRequest 注解的方法
     */
    @Before("limitControllerPoint() && @annotation(rateLimiter))")
    public void doBeforeLimit(JoinPoint pjp, RateLimiter rateLimiter) {
        // 解析注解值
        int count = rateLimiter.count();
        long timeout = rateLimiter.timeout();
        TimeUnit timeunit = rateLimiter.timeunit();
        String rateLimitKey = getRateLimitKey(pjp, rateLimiter);
        long realTimeout = timeunit.toSeconds(timeout);

        try {
            Long visitCount = redisTemplate.execute(limitScript, Collections.singletonList(rateLimitKey), count, realTimeout);
            log.info("限制策略：{}，限制请求数：{}, 当前请求数：{}， redis 缓存的 key：{}",
                    rateLimiter.limitStrategy(), count, visitCount, rateLimitKey);
            if (visitCount == null || visitCount.intValue() > count) {
                throw new ShopCustomException(ResultCodeEnum.LIMIT_MAX_ERROR);
            }
        } catch (ShopCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍后重试！");
        }
    }


    private String getRateLimitKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        String methodName = methodSignature.getMethod().getName();
        String className = methodSignature.getMethod().getDeclaringClass().getName();
        log.info("当前限流的方法：{}", methodName);
        log.info("当前限流的类名：{}", className);
        // 默认 key 前缀为缓存空间的名字
        StringBuilder sb = new StringBuilder(rateLimiter.cacheNames());
        // IP 限流
        if (LimitStrategy.IP == rateLimiter.limitStrategy()) {
            String ip = RequestContext.getRemoteAddr();
            log.info("当前限流的 ip 是：{}", ip);
            sb.append(ip).append(":");
        }
        // 方法级别限流
        sb.append(className).append(":").append(methodName);
        String keyValue = rateLimiter.key();
        // 解析 key
        if (StrUtil.isNotBlank(keyValue)) {
            sb.append(":");
            // el 表达式
            if (keyValue.startsWith("#")) {
                // 获取方法参数和参数值
                String[] parameterNames = methodSignature.getParameterNames();
                Object[] args = joinPoint.getArgs();
                sb.append(ParseElUtils.parseElKey(keyValue, parameterNames, args));
            } else {
                sb.append(keyValue);
            }
        }
        return sb.toString();
    }
}
