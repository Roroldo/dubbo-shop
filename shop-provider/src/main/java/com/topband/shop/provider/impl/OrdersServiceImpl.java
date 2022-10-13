package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.GoodsService;
import com.topband.shop.api.OrdersService;
import com.topband.shop.api.UserVoucherService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.OrdersDTO;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.dto.UserVoucherDTO;
import com.topband.shop.entity.Orders;
import com.topband.shop.provider.mapper.OrdersMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.GoodsVO;
import com.topband.shop.view.OrdersVO;
import com.topband.shop.view.VoucherVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.RedisConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: OrdersServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: OrdersServiceImpl
 * @date 2022/9/13 17:28
 */
@DubboService
@Slf4j
public class OrdersServiceImpl implements OrdersService {
    @Resource
    private GoodsService goodsService;

    @Resource
    private UserVoucherService userVoucherService;

    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public Result spikeGoods(OrdersDTO ordersDTO) {
        // 根据商品 id 查询商品信息
        GoodsVO goodsVO = goodsService.selectById(ordersDTO.getGoodsId());
        if (goodsVO == null) {
            return Result.fail();
        }
        // 商品库存不足
        if (goodsVO.getTotal() <= 0) {
            return Result.fail(ResultCodeEnum.GOODS_TOTAL_ERROR);
        }

        Orders orders = BeanUtil.copyProperties(ordersDTO, Orders.class, "ordersSerial");
        BigDecimal goodsPrice = goodsVO.getGoodsPrice();
        VoucherVO voucherVO = userVoucherService.selectNotExpiredVoucherByUserId(ordersDTO.getUserId());
        // 计算订单价格
        if (voucherVO != null) {
            // 可以享受折扣
            if (isVoucherPriceGtGoodsPrice(voucherVO.getVoucherPrice(), goodsPrice)) {
                // 优惠卷大于商品金额
                orders.setOrderPrice(goodsPrice);
            } else {
                orders.setVoucherId(voucherVO.getId());
                orders.setOrderPrice(goodsPrice.subtract(voucherVO.getVoucherPrice()));
            }
        } else {
            // 优惠卷不存在
            orders.setOrderPrice(goodsPrice);
        }

        // 接口的幂等性保证，上游服务必须传递一个订单流水号过来
        String orderSerialKey = SUCCESS_ORDERS_SERIAL_NUMBER_KEY + ordersDTO.getOrdersSerial();
        RLock lock = redissonClient.getLock(GOODS_LOCK_KEY + goodsVO.getId());
        try {
            lock.lock();
            OrdersService ordersService = (OrdersService) AopContext.currentProxy();
            Result createOrderResult = ordersService.createOrders(orders, orderSerialKey);
            // 订单创建成功
            if (createOrderResult.getData() != null) {
                // 将订单流水号加入 redis 中，表示此订单已经创建成功
                redisTemplate.opsForValue().set(orderSerialKey, createOrderResult.getData(),
                        SUCCESS_ORDERS_SERIAL_NUMBER_KEY_EXPIRE, TimeUnit.MINUTES);
            }
            return createOrderResult;
        } finally {
            lock.unlock();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createOrders(Orders orders, String orderSerialKey) {
        // 订单幂等性校验
        if (redisTemplate.opsForValue().get(orderSerialKey) != null) {
            return Result.fail(ResultCodeEnum.ORDERS_REPEAT_ERROR);
        }
        Long goodsId = orders.getGoodsId();
        Long voucherId = orders.getVoucherId();
        Long userId = orders.getUserId();
        if (goodsService.selectById(goodsId).getTotal() <= 0) {
            // 库存不足
            return Result.fail(ResultCodeEnum.GOODS_TOTAL_ERROR);
        }
        // 扣减库存
        goodsService.updateTotalById(goodsId);
        if (voucherId != null) {
            // 删除用户优惠卷
            userVoucherService.deleteUserVoucher(userId, voucherId);
        }
        orders.setId(SnowFlakeUtils.nextId());
        orders.setCreateTime(new Date());
        orders.setUpdateTime(new Date());
        ordersMapper.save(orders);
        return Result.ok(orders.getId());
    }

    @Override
    public boolean existByUserIdAndVoucherId(UserVoucherDTO userVoucherDTO) {
        return ordersMapper.countByUserIdAndVoucherId(userVoucherDTO.getUserId(),
                userVoucherDTO.getVoucherId()) > 0;
    }

    @Override
    public ResultPage list(PageQueryDTO pageQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(pageQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(pageQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<OrdersVO> ordersVOList = ordersMapper.list(pageQueryDTO.getStart(), pageQueryDTO.getEnd());
        PageInfo<OrdersVO> pageInfo = new PageInfo<>(ordersVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<OrdersVO> listManual(PageQueryDTO pageQueryDTO) {
        return ordersMapper.listManual(pageQueryDTO);
    }

    @Override
    public boolean existByGoodsId(Long goodsId) {
        return ordersMapper.countByGoodsId(goodsId) > 0;
    }


    /**
     * 优惠卷金额比商品金额大
     */
    private boolean isVoucherPriceGtGoodsPrice(BigDecimal voucherPrice, BigDecimal goodsPrice) {
        return voucherPrice.compareTo(goodsPrice) == 1;
    }
}
