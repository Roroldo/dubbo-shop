package com.topband.shop.consumer.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.exception.ShopCustomException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.topband.shop.constants.BusinessConstants.GOODS_EXCEL_PATTERN_HEAD_MAP;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: CheckHeadListener
 * @packageName: com.topband.shop.consumer.excel
 * @description: CheckHeadListener 校验表头
 * @date 2022/9/17 13:58
 */
@Slf4j
public class CheckHeadListener extends AbstractUploadsListener {
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> readHeadMap = ConverterUtils.convertToStringMap(headMap, context);
        if (readHeadMap.size() != GOODS_EXCEL_PATTERN_HEAD_MAP.size()) {
            throw new ShopCustomException(ResultCodeEnum.EXCEL_HEAD_ERROR);
        }
        // 校验表头
        for (Integer colIndex : GOODS_EXCEL_PATTERN_HEAD_MAP.keySet()) {
            String patternValue = GOODS_EXCEL_PATTERN_HEAD_MAP.get(colIndex);
            String realValue = readHeadMap.get(colIndex);
            if (!patternValue.equals(realValue)) {
                log.error("校验 excel 头信息异常");
                throw new ShopCustomException(ResultCodeEnum.EXCEL_HEAD_ERROR);
            }
        }
    }
}
