package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.OrdersDTO;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.dto.UserVoucherDTO;
import com.topband.shop.entity.Orders;
import com.topband.shop.view.OrdersVO;

import java.util.List;

public interface OrdersService {
    Result spikeGoods(OrdersDTO ordersDTO);
    
    Result createOrders(Orders orders, String ordersSerial);

    /**
     * 判断优惠卷以前有没有被用户消费过
     */
    boolean existByUserIdAndVoucherId(UserVoucherDTO userVoucherDTO);

    ResultPage list(PageQueryDTO pageQueryDTO);

    List<OrdersVO> listManual(PageQueryDTO pageQueryDTO);

    boolean existByGoodsId(Long goodsId);
}
