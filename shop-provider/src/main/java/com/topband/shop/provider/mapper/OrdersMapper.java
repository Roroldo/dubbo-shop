package com.topband.shop.provider.mapper;

import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.Orders;
import com.topband.shop.view.OrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: OrdersMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: OrdersMapper
 * @date 2022/9/14 8:40
 */
public interface OrdersMapper {
    void save(Orders orders);

    int countByUserIdAndVoucherId(@Param("userId")Long userId, @Param("voucherId") Long voucherId);

    List<OrdersVO> list(@Param("start") Date start, @Param("end") Date end);

    List<OrdersVO> listManual(PageQueryDTO pageQueryDTO);

    int countByGoodsId(Long goodsId);
}
