package com.topband.shop.consumer.aop;

import cn.hutool.core.util.StrUtil;
import com.topband.shop.api.LogRecordService;
import com.topband.shop.base.Result;
import com.topband.shop.consumer.anno.log.LogRecordAnno;
import com.topband.shop.entity.LogRecord;
import com.topband.shop.utils.AdminHolder;
import com.topband.shop.utils.ParseElUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RateLimiterInterceptor
 * @packageName: com.topband.shop.consumer.config
 * @description: 日志 aop
 * @date 2022/9/15 13:57
 */
@Slf4j
@Aspect
@Component
public class LogAop implements HandlerInterceptor {
    @DubboReference
    private LogRecordService logRecordService;


    @Pointcut("within(com.topband.shop.consumer.controller.*)")
    private void limitControllerPoint() {
    }


    @AfterReturning(value = "limitControllerPoint() && @annotation(logRecordAnno))", returning = "methodResult")
    public void doAfterReturning(JoinPoint joinPoint, Object methodResult, LogRecordAnno logRecordAnno) {
        // 方法没有返回值
        if (methodResult == null) {
            saveLogRecord(joinPoint, logRecordAnno);
            return;
        }
        if (methodResult instanceof Result) {
            Result<?> result = (Result<?>) methodResult;
            // 请求方法执行成功
            if (result.getCode() == 0) {
                saveLogRecord(joinPoint, logRecordAnno);
            }
        }
    }

    private void saveLogRecord(JoinPoint joinPoint, LogRecordAnno logRecordAnno) {
        LogRecord logRecord = new LogRecord();
        logRecord.setId(SnowFlakeUtils.nextId());
        logRecord.setAdminId(AdminHolder.getAdmin().getId());
        logRecord.setCreateTime(new Date());
        logRecord.setUpdateTime(new Date());
        String recordContent = logRecordAnno.value().getLogType();
        String content = logRecordAnno.content();
        if (StrUtil.isNotBlank(content) && content.startsWith("#")) {
            MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
            String[] parameterNames = methodSignature.getParameterNames();
            Object[] args = joinPoint.getArgs();
            recordContent += ParseElUtils.parseElKey(content, parameterNames, args);
        }
        logRecord.setContent(recordContent);
        logRecordService.save(logRecord);
    }
}
