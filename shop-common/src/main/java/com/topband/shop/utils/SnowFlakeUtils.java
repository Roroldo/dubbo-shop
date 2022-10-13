package com.topband.shop.utils;

import cn.hutool.core.lang.Snowflake;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: SnowFlake
 * @packageName: com.topband.shop.utils
 * @description: 雪花算法
 * @date: 2022-09-08 19:46
 */
public class SnowFlakeUtils {
    private static final Snowflake snowflake;

    static {
        snowflake = new Snowflake(1, 2, true);
    }

    /**
     * 产生下一个ID
     */
    public static long nextId() {
        return snowflake.nextId();
    }
}