package com.topband.shop.constants;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RedisConstants
 * @packageName: com.topband.shop.constants
 * @description: Redis 常量
 * @date 2022/9/9 14:37
 */
public class RedisConstants {
    public static final String RATE_LIMITER_KEY_PREFIX = "hyj:shop:rate_limit:";
    public static final String BASIC_CLIENT_KEY = "hyj:shop:client:";
    public static final String BASIC_WEB_KEY = "hyj:shop:web:";
    public static final String CLIENT_TOKEN_KEY = BASIC_CLIENT_KEY + "token:";
    public static final String WEB_TOKEN_KEY = BASIC_WEB_KEY + "token:";
    public static final String CLIENT_LOGIN_KEY = BASIC_CLIENT_KEY + "login:";
    public static final String EMAIL_CHECK_CODE_KEY = BASIC_WEB_KEY + "checkCode:reset:";
    public static final String WEB_LOGIN_KEY = BASIC_WEB_KEY + "login:";
    public static final String WEB_LOGIN_CHECK_CODE_KEY = BASIC_WEB_KEY + "checkCode:login:";
    public static final String ROLE_PERMISSIONS_SET_KEY = BASIC_WEB_KEY + "permission:role:";
    public static final String NEED_ROLE_PERMISSIONS_SET_KEY = BASIC_WEB_KEY + "permission:role:need";
    public static final String VOUCHER_LOCK_KEY = BASIC_CLIENT_KEY + "lock:voucher:";
    public static final String GOODS_LOCK_KEY = BASIC_CLIENT_KEY + "lock:goods:";
    public static final String SUCCESS_ORDERS_SERIAL_NUMBER_KEY = BASIC_CLIENT_KEY + "orders:";
    public static final String USER_ACTIVE_DATA_KEY = BASIC_CLIENT_KEY + "user:data:";
    public static final String USER_ACTIVE_KEY = USER_ACTIVE_DATA_KEY + "active";
    public static final String USER_NEW_KEY = USER_ACTIVE_DATA_KEY + "new";

    public static final long TOKEN_EXPIRE = 2L;
    public static final long EMAIL_CHECK_CODE_EXPIRE = 10L;
    public static final long SUCCESS_ORDERS_SERIAL_NUMBER_KEY_EXPIRE = 2L;
    public static final long ROLE_PERMISSIONS_SET_KEY_EXPIRE = 6L;
    public static final long WEB_LOGIN_CHECK_CODE_KEY_EXPIRE = 3L;
    public static final long USER_ACTIVE_DATA_EXPIRE = ChronoUnit.SECONDS.between(LocalDateTime.now(),
            LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0));
}
