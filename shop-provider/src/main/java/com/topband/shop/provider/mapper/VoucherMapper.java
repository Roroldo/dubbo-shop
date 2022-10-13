package com.topband.shop.provider.mapper;

import com.topband.shop.entity.Voucher;
import com.topband.shop.view.VoucherVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: VoucherMapper
 * @date 2022/9/11 17:19
 */
public interface VoucherMapper {
    List<VoucherVO> list(@Param("start") Date start, @Param("end") Date end,
                         @Param("isFindAll") boolean isFindAll);
    boolean save(Voucher voucher);
    int countByName(String voucherName);

    VoucherVO selectById(Long voucherId);

    boolean updateById(@Param("voucherId") Long voucherId, @Param("updateTime") Date updateTime);
}
