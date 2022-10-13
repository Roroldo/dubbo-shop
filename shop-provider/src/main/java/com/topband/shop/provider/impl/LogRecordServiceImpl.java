package com.topband.shop.provider.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.LogRecordService;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.LogRecord;
import com.topband.shop.provider.mapper.LogRecordMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.view.LogRecordVO;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LogRecordServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: LogRecordServiceImpl
 * @date 2022/9/17 20:48
 */
@DubboService
public class LogRecordServiceImpl implements LogRecordService {
    @Resource
    private LogRecordMapper logRecordMapper;

    @Override
    public void save(LogRecord logRecord) {
        logRecordMapper.save(logRecord);
    }

    @Override
    public ResultPage list(PageQueryDTO pageQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(pageQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(pageQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<LogRecordVO> logRecordVOList = logRecordMapper.list(pageQueryDTO);
        PageInfo<LogRecordVO> pageInfo = new PageInfo<>(logRecordVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }
}
