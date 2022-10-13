package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.VoucherService;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.dto.VoucherDTO;
import com.topband.shop.entity.Voucher;
import com.topband.shop.provider.mapper.VoucherMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.VoucherVO;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: VoucherServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: VoucherServiceImpl
 * @date 2022/9/11 17:19
 */
@DubboService
public class VoucherServiceImpl implements VoucherService {
    @Resource
    private VoucherMapper voucherMapper;

    @Override
    public ResultPage<List<VoucherVO>> list(PageQueryDTO pageQueryDTO, boolean isFindAll) {
        int currentPage = CheckPageUtils.getCurrentPage(pageQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(pageQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<VoucherVO> voucherVOList = voucherMapper.list(pageQueryDTO.getStart(),
                pageQueryDTO.getEnd(), isFindAll);
        PageInfo<VoucherVO> pageInfo = new PageInfo<>(voucherVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public boolean add(VoucherDTO voucherDTO) {
        if (voucherMapper.countByName(voucherDTO.getName()) > 0) {
            return false;
        }
        Voucher voucher = BeanUtil.copyProperties(voucherDTO, Voucher.class);
        voucher.setId(SnowFlakeUtils.nextId());
        voucher.setCreateTime(new Date());
        voucher.setUpdateTime(new Date());
        return voucherMapper.save(voucher);
    }

    @Override
    public VoucherVO selectById(Long voucherId) {
        return voucherMapper.selectById(voucherId);
    }

    @Override
    public boolean updateById(Long voucherId) {
        return voucherMapper.updateById(voucherId, new Date());
    }
}
