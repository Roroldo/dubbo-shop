package com.topband.shop.provider.mapper;

import com.topband.shop.entity.UserVoucher;
import com.topband.shop.view.VoucherVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucherMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: UserVoucherMapper
 * @date 2022/9/13 15:28
 */
public interface UserVoucherMapper {
    int countNotExpiredVoucher(long userId);

    void save(UserVoucher userVoucher);

    VoucherVO selectNotExpiredVoucherByUserId(Long userId);

    void deleteByUserIdAndVoucherId(@Param("userId") Long userId,
                                    @Param("voucherId") Long voucherId,
                                    @Param("updateTime") Date updateTime);
}
