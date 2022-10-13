package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.GoodsQueryDTO;
import com.topband.shop.entity.Goods;
import com.topband.shop.view.GoodsVO;

import java.util.Set;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsService
 * @packageName: com.topband.shop.api
 * @description: GoodsService
 * @date 2022/9/12 13:32
 */
public interface GoodsService {
    ResultPage list(GoodsQueryDTO goodsQueryDTO);

    GoodsVO selectById(Long goodsId);

    void updateTotalById(Long goodsId);

    Result save(GoodsVO goodsVO);

    Result updateById(GoodsVO goodsVO);

    boolean repeatName(String name);

    void saveBatch(Set<Goods> cacheGoodsSet);

    Result deleteById(Long goodsId);
}
