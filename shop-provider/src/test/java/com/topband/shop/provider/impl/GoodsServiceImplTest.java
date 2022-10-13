package com.topband.shop.provider.impl;

import com.topband.shop.api.GoodsService;
import com.topband.shop.entity.Goods;
import com.topband.shop.utils.SnowFlakeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsServiceImplTest
 * @packageName: com.topband.shop.provider.impl
 * @description: GoodsServiceImplTest
 * @date 2022/9/17 16:42
 */
@SpringBootTest
public class GoodsServiceImplTest {
    @Resource
    private GoodsService goodsService;

    @Test
    public void testBatchSave() {
        Goods goods = new Goods();
        goods.setId(SnowFlakeUtils.nextId());
        goods.setName("todooo");
        goods.setTotal(1);
        goods.setGoodsPrice(new BigDecimal(123));
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());

        Goods goods2 = new Goods();
        goods2.setId(SnowFlakeUtils.nextId());
        goods2.setName("todooo212");
        goods2.setTotal(1);
        goods2.setGoodsPrice(new BigDecimal(123));
        goods2.setCreateTime(new Date());
        goods2.setUpdateTime(new Date());
        Set<Goods> goodsSet = new HashSet<>();
        goodsSet.add(goods);
        goodsSet.add(goods2);
        goodsService.saveBatch(goodsSet);
    }

    @Test
    public void testEqual() {
        Set<Goods> goodsSet = new HashSet<>();
        Goods goods = new Goods();
        goods.setName("todooo");
        goods.setUpdateTime(new Date());

        Goods goods2 = new Goods();
        goods2.setName("todooo");
        goodsSet.add(goods);
        goodsSet.add(goods2);
        assert goodsSet.size() == 1;
    }
}
