package com.topband.shop.consumer.anno.root;

import java.lang.annotation.*;

/**
 * 标记当前登录用户必须是超级管理员才能进行的操作
 * @author huangyijun
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IsAdmin {
}
