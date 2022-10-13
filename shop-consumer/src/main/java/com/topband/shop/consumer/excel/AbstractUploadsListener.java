package com.topband.shop.consumer.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.topband.shop.entity.Goods;

import java.util.Map;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AbstractUploadsListener
 * @packageName: com.topband.shop.consumer.excel
 * @description: AbstractUploadsListener
 * @date 2022/9/17 14:12
 */
public class AbstractUploadsListener implements ReadListener<Goods> {
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {

    }

    @Override
    public void invoke(Goods goods, AnalysisContext analysisContext) {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
