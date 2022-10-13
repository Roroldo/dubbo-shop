package com.topband.shop.utils;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: CheckCodeUtils
 * @packageName: com.topband.shop.utils
 * @description: 验证码工具类
 * @date 2022/9/9 11:24
 */
public class CheckCodeUtils {
    private static final String BASE_NUMBER = "0123456789";
    private static final String BASE_LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String BASE_UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String BASE_CHAR_NUMBER = BASE_NUMBER + BASE_LOWER_CHAR + BASE_UPPER_CHAR;


    public static String createEmailCheckCode() {
        return RandomUtil.randomNumbers(6);
    }

    public static RandomGenerator createLoginCheckCode() {
        return new RandomGenerator(BASE_CHAR_NUMBER, 4);
    }

    /**
     * 校验验证码是否匹配
     */
    public static boolean isMatch(String code, String cacheCode) {
        return StrUtil.equalsIgnoreCase(code, cacheCode);
    }
}
