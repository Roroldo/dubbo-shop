package com.topband.shop.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: BusinessConstants
 * @packageName: com.topband.shop.constants
 * @description: BusinessConstants
 * @date 2022/9/11 16:04
 */
public class BusinessConstants {
    public static final int DAY_MIN = 7;
    public static final int DAY_MAX = 30;
    public static final String CHECK_CODE_COOKIE_NAME = "codeImg";

    public static final int ADMIN_LOCKED = 1;
    public static final String LOCKED = "冻结";
    public static final String NORMAL = "正常";

    public static final int EXPORT_ORDERS_MAX = 10000;
    public static final int EXPORT_ORDERS_PAGE_SIZE = 500;
    public static final String EXPORT_ORDERS_EXCEL_NAME = "订单列表";
    public static final String EXPORT_ORDERS_EXCEL_SHEET_NAME = "订单详情";

    public static final String IMAGE_SUFFIX = "image/png";
    public static final long IMAGE_MAX_SIZE = 1024 * 1024 * 10;
    public static final String GOODS_IMAGES_DIR = "/goodsImg/";
    public static final String GOODS_IMAGES_PATH = "file:" + System.getProperty("user.dir") + GOODS_IMAGES_DIR;
    public static final String REAL_GOODS_IMAGES_PATH = System.getProperty("user.dir") + GOODS_IMAGES_DIR;

    public static final long EXCEL_MAX_SIZE = 1024 * 1024 * 100;
    public static final String EXCEL_FILE1 = ".xlsx";
    public static final String EXCEL_FILE2 = ".xls";
    public static final String EXPORT_GOODS_EXCEL_FEED_BACK_SHEET_NAME = "商品导入反馈信息";
    public static final String EXPORT_GOODS_EXCEL_ORIGINAL_SHEET_NAME = "商品信息";
    public static final String GOODS_EXCEL_DIR = "/goodsExcel/";
    public static final String REAL_GOODS_EXCEL_PATH = System.getProperty("user.dir") + GOODS_EXCEL_DIR;

    public static final String ROOT_ROLE = "超级管理员";

    public static final String GOODS_EXCEL_HEAD0_NAME = "商品名称";
    public static final String GOODS_EXCEL_HEAD1_NAME = "商品库存";
    public static final String GOODS_EXCEL_HEAD2_NAME = "商品价格";
    public static final Map<Integer, String> GOODS_EXCEL_PATTERN_HEAD_MAP = new HashMap<>();

    static {
        GOODS_EXCEL_PATTERN_HEAD_MAP.put(0, GOODS_EXCEL_HEAD0_NAME);
        GOODS_EXCEL_PATTERN_HEAD_MAP.put(1, GOODS_EXCEL_HEAD1_NAME);
        GOODS_EXCEL_PATTERN_HEAD_MAP.put(2, GOODS_EXCEL_HEAD2_NAME);
    }
}
