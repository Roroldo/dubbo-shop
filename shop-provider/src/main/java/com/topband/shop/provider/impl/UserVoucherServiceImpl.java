package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import com.topband.shop.api.OrdersService;
import com.topband.shop.api.UserVoucherService;
import com.topband.shop.api.VoucherService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.dto.UserVoucherDTO;
import com.topband.shop.entity.UserVoucher;
import com.topband.shop.provider.mapper.UserVoucherMapper;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.VoucherVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

import static com.topband.shop.constants.RedisConstants.VOUCHER_LOCK_KEY;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserVoucherServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: UserVoucherServiceImpl
 * @date 2022/9/13 15:07
 */
@DubboService
@Slf4j
public class UserVoucherServiceImpl implements UserVoucherService {
    @Resource
    private VoucherService voucherService;

    @Resource
    private UserVoucherMapper userVoucherMapper;

    @Resource
    private OrdersService ordersService;

    @Resource
    private RedissonClient redissonClient;

    @Override
    public Result spikeVoucher(UserVoucherDTO userVoucherDTO) {
        Long userId = userVoucherDTO.getUserId();
        // 根据用户 id 判断是否拥有未过期的优惠卷
        if (this.existNotExpiredVoucher(userId)) {
            return Result.fail(ResultCodeEnum.VOUCHER_EXIST_NOT_EXPIRED_ERROR);
        }
        // 根据 voucherId 查询优惠卷信息
        VoucherVO voucherVO = voucherService.selectById(userVoucherDTO.getVoucherId());
        if (voucherVO == null) {
            return Result.fail();
        }
        // 判断是否过期
        if (voucherVO.getExpireTime().before(new Date())) {
            return Result.fail(ResultCodeEnum.VOUCHER_EXPIRE_ERROR);
        }
        // 判断库存
        if (voucherVO.getCount() <= 0) {
            return Result.fail(ResultCodeEnum.VOUCHER_COUNT_ERROR);
        }
        // 校验优惠卷是否已经被使用过
        if (ordersService.existByUserIdAndVoucherId(userVoucherDTO)) {
            return Result.fail(ResultCodeEnum.ORDERS_VOUCHER_REPEAT_ERROR);
        }
        // 使用用户 id 作为锁的 key，防止一个用户抢夺多个优惠卷
        // 获取锁这里打个断点，不执行，等 dubbo 重试三次，依次放行三个断点，幂等性就会被破坏
        // 因此 createUserVoucher 里面还需要再次校验幂等性
        RLock lock = redissonClient.getLock(VOUCHER_LOCK_KEY + userId);
        // 锁的默认过期时间是 30s，有自动续费机制，防止业务没有完成就释放锁
        boolean isLock = lock.tryLock();
        if (!isLock) {
            return Result.fail(ResultCodeEnum.VOUCHER_SPIKE_REPEAT_ERROR);
        }
        try {
            // 获取 userVoucherService 的事务代理对象
            UserVoucherService userVoucherService = (UserVoucherService) AopContext.currentProxy();
            return userVoucherService.createUserVoucher(userVoucherDTO);
        } finally {
            // 必须等事务提交后，才能释放锁
            lock.unlock();
        }
    }

    /*
        方法权限不能是私有，否则事务会失效。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result createUserVoucher(UserVoucherDTO userVoucherDTO) {
        // 一人一单校验，幂等性校验
        if (this.existNotExpiredVoucher(userVoucherDTO.getUserId())) {
            return Result.fail(ResultCodeEnum.VOUCHER_EXIST_NOT_EXPIRED_ERROR);
        }
        // 更新优惠卷库存，乐观锁解决超卖 count > 0 才更新（update 的行锁）
        boolean success = voucherService.updateById(userVoucherDTO.getVoucherId());
        if (!success) {
            return Result.fail(ResultCodeEnum.VOUCHER_COUNT_ERROR);
        }
        UserVoucher userVoucher = BeanUtil.copyProperties(userVoucherDTO, UserVoucher.class);
        userVoucher.setId(SnowFlakeUtils.nextId());
        userVoucher.setCreateTime(new Date());
        userVoucher.setUpdateTime(new Date());
        userVoucherMapper.save(userVoucher);
        return Result.ok();
    }

    @Override
    public VoucherVO selectNotExpiredVoucherByUserId(Long userId) {
        return userVoucherMapper.selectNotExpiredVoucherByUserId(userId);
    }

    @Override
    public void deleteUserVoucher(Long userId, Long voucherId) {
        userVoucherMapper.deleteByUserIdAndVoucherId(userId, voucherId, new Date());
    }

    @Override
    public boolean existNotExpiredVoucher(Long userId) {
        return userVoucherMapper.countNotExpiredVoucher(userId) > 0;
    }
}
