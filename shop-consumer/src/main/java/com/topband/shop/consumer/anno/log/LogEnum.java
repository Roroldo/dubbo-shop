package com.topband.shop.consumer.anno.log;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日志类型
 * @author huangyijun
 */
@Getter
@AllArgsConstructor
public enum LogEnum {
    ADD_GOODS("新增商品:"),
    UPDATE_OR_DELETE_GOODS("修改商品:删除商品"),
    IMPORT_GOODS_EXCEL("导入商品:"),
    EXPORT_ORDERS_EXCEL("导出订单");

    private String logType;
}
