package com.topband.shop.provider.impl;

import com.topband.shop.api.UserVoucherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucherServiceImplTest
 * @packageName: com.topband.shop.provider.impl
 * @description: 测试事务失效
 * @date 2022/9/13 16:16
 */
@SpringBootTest
public class UserVoucherServiceImplTest {
    @Resource
    private UserVoucherService userVoucherService;

    @Test
    public void testSpikeVoucher() {
        userVoucherService.spikeVoucher(null);
    }
}
