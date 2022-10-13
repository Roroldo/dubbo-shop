package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.GoodsService;
import com.topband.shop.api.OrdersService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.GoodsQueryDTO;
import com.topband.shop.entity.Goods;
import com.topband.shop.provider.mapper.GoodsMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GoodsServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: GoodsServiceImpl
 * @date 2022/9/12 13:51
 */
@Slf4j
@DubboService
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private OrdersService ordersService;

    @Override
    public ResultPage list(GoodsQueryDTO goodsQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(goodsQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(goodsQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<GoodsVO> goodsVOList = goodsMapper.list(goodsQueryDTO.getName());
        PageInfo<GoodsVO> pageInfo = new PageInfo<>(goodsVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public GoodsVO selectById(Long goodsId) {
        return goodsMapper.selectById(goodsId);
    }

    @Override
    public void updateTotalById(Long goodsId) {
        goodsMapper.updateTotalById(goodsId, new Date());
    }

    @Override
    public Result save(GoodsVO goodsVO) {
        // 校验商品名称
        if (goodsMapper.countByName(goodsVO.getName(), null) > 0) {
            return Result.fail(ResultCodeEnum.GOODS_NAME_REPEAT_ERROR);
        }
        Goods goods = BeanUtil.copyProperties(goodsVO, Goods.class);
        initGoods(goods);
        if (goodsMapper.save(goods)) {
            return Result.ok();
        }
        return Result.fail();
    }

    private void initGoods(Goods goods) {
        goods.setId(SnowFlakeUtils.nextId());
        if (goods.getCreateTime() == null) {
            goods.setCreateTime(new Date());
        }
        goods.setUpdateTime(new Date());
    }

    @Override
    public Result updateById(GoodsVO goodsVO) {
        if (goodsMapper.countByName(goodsVO.getName(), goodsVO.getId()) > 0) {
            return Result.fail(ResultCodeEnum.GOODS_NAME_REPEAT_ERROR);
        }
        Goods goods = BeanUtil.copyProperties(goodsVO, Goods.class);
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateById(goods)) {
            return Result.ok();
        }
        return Result.fail();
    }

    @Override
    public boolean repeatName(String name) {
        return goodsMapper.countByName(name, null) > 0;
    }

    @Override
    public void saveBatch(Set<Goods> cacheGoodsSet) {
        if (cacheGoodsSet.size() == 0) {
            return;
        }
        cacheGoodsSet.forEach(this::initGoods);
        goodsMapper.saveBatch(cacheGoodsSet);
    }

    @Override
    public Result deleteById(Long goodsId) {
        if (ordersService.existByGoodsId(goodsId)) {
            return Result.fail(ResultCodeEnum.GOODS_DELETE_ERROR);
        }
        goodsMapper.deleteById(goodsId, new Date());
        return Result.ok();
    }
}
