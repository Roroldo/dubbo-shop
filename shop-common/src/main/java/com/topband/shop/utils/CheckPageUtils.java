package com.topband.shop.utils;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: CheckPageUtils
 * @packageName: com.topband.shop.utils
 * @description: CheckPageUtils
 * @date 2022/9/12 13:58
 */
public class CheckPageUtils {
    public static final int DEFAULT_PAGE_SIZE = 10;

    public static int getCurrentPage(Integer currentPage) {
        return currentPage == null || currentPage <= 0 ? 1 : currentPage;
    }

    public static int getPageSize(Integer pageSize) {
        return pageSize == null || pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
