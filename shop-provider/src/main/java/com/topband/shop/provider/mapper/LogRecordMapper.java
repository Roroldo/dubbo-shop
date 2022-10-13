package com.topband.shop.provider.mapper;

import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.LogRecord;
import com.topband.shop.view.LogRecordVO;

import java.util.List;

public interface LogRecordMapper {
    void save(LogRecord logRecord);
    List<LogRecordVO> list(PageQueryDTO pageQueryDTO);
}
