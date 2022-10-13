package com.topband.shop.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: RegexUtils
 * @packageName: com.topband.shop.utils
 * @description: 正则校验
 * @date: 2022-09-09 9:22
 */
public class RegexUtils {
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:符合，false：不符合
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:符合，false：不符合
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.LOGIN_CODE_REGEX);
    }



    /**
     * 是否是无效密码格式
     * @param password 要校验的密码
     * @return true:符合，false：不符合
     */
    public static boolean isPasswordInvalid(String password){
        return mismatch(password, RegexPatterns.PASSWORD_REGEX);
    }

    /**
     * 校验是否不符合正则格式
     */
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
