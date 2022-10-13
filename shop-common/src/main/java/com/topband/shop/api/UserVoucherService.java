package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.dto.UserVoucherDTO;
import com.topband.shop.view.VoucherVO;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucherService
 * @packageName: com.topband.shop.api
 * @description: UserVoucherService
 * @date 2022/9/13 15:04
 */
public interface UserVoucherService {
    /**
     * 秒杀优惠卷
     */
    Result spikeVoucher(UserVoucherDTO userVoucherDTO);
    
    /**
     * 用户是否有未过期的优惠卷
     */
    boolean existNotExpiredVoucher(Long userId);

    /**
     * 创建用户优惠卷
     */
    Result createUserVoucher(UserVoucherDTO userVoucherDTO);

    VoucherVO selectNotExpiredVoucherByUserId(Long userId);

    void deleteUserVoucher(Long userId, Long voucherId);
}
