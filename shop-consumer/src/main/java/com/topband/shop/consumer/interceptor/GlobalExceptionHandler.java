package com.topband.shop.consumer.interceptor;


import com.topband.shop.base.Result;
import com.topband.shop.exception.ShopCustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler
 * @author huangyijun
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        log.error("系统异常：{}", ExceptionUtils.getStackTrace(e));
        return Result.fail();
    }

    @ExceptionHandler(ShopCustomException.class)
    public Result error(ShopCustomException e) {
        log.error("商城业务异常：{}", ExceptionUtils.getStackTrace(e));
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result handleValidatedException(Exception e) {
        Result result = Result.build();
        log.info("参数异常：{}", ExceptionUtils.getStackTrace(e));
        result.setCode(-1);
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            result.setMessage(ex.getBindingResult().getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(";")));
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ex = (ConstraintViolationException) e;
            result.setMessage(ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(";")));
        } else if (e instanceof BindException) {
            BindException ex = (BindException) e;
            result.setMessage(ex.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(";")));
        }
        return result;
    }
}
