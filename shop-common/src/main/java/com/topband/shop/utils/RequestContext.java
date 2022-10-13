package com.topband.shop.utils;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RequestContext
 * @packageName: com.topband.four.utils
 * @description: 保存 ip 地址
 * @date 2022/8/25 11:32
 */
public class RequestContext {
    private static final ThreadLocal<String> REMOTE_ADDR = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return REMOTE_ADDR.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        REMOTE_ADDR.set(remoteAddr);
    }

    public static void removeRemoteAddr() {
        REMOTE_ADDR.remove();
    }
}
