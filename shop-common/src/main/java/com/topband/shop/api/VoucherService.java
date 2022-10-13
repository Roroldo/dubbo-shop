package com.topband.shop.api;

import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.dto.VoucherDTO;
import com.topband.shop.view.VoucherVO;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherService
 * @packageName: com.topband.shop.api
 * @description: VoucherService
 * @date 2022/9/11 17:16
 */
public interface VoucherService {
    /**
     * isFindAll 是否查询过期的卷
     */
    ResultPage list(PageQueryDTO pageQueryDTO, boolean isFindAll);

    boolean add(VoucherDTO voucherDTO);

    VoucherVO selectById(Long voucherId);

    boolean updateById(Long voucherId);
}
