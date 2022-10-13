package com.topband.shop.utils;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: RegexPatterns
 * @packageName: com.topband.shop.utils
 * @description: 常用的正则表达式
 * @date: 2022-09-09 9:15
 */
public abstract class RegexPatterns {
    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 密码正则。8~20位的字母、数字、下划线
     */
    public static final String PASSWORD_REGEX = "^[^\\u4e00-\\u9fa5 ]{8,20}$";

    /**
     * 登录验证码正则, 4位数字或字母
     */
    public static final String LOGIN_CODE_REGEX = "^[a-zA-Z\\d]{4}$";
}
