package com.topband.shop.api;

import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.LogRecord;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LogRecordService
 * @packageName: com.topband.shop.api
 * @description: LogRecordService
 * @date 2022/9/17 20:47
 */
public interface LogRecordService {
    void save(LogRecord logRecord);
    ResultPage list(PageQueryDTO pageQueryDTO);
}
