package com.topband.shop.provider.mapper;

import com.topband.shop.entity.Goods;
import com.topband.shop.view.GoodsVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: GoodsMapper
 * @date 2022/9/12 13:43
 */
public interface GoodsMapper {
    List<GoodsVO> list(String goodsName);

    GoodsVO selectById(Long goodsId);

    void updateTotalById(Long goodsId,  @Param("updateTime") Date updateTime);

    int countByName(@Param("name") String name, @Param("id") Long id);

    int countByCategory(@Param("category") String category, @Param("id") Long id);

    boolean save(Goods goods);

    boolean updateById(Goods goods);

    void saveBatch(Set<Goods>  cacheGoodsSet);

    void deleteById(@Param("goodsId") Long goodsId, @Param("updateTime") Date updateTime);
}
