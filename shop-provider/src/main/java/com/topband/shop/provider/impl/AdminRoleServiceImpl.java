package com.topband.shop.provider.impl;

import com.topband.shop.api.AdminRoleService;
import com.topband.shop.dto.AdminEditDTO;
import com.topband.shop.entity.AdminRole;
import com.topband.shop.entity.Role;
import com.topband.shop.provider.mapper.AdminRoleMapper;
import com.topband.shop.utils.SnowFlakeUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminRoleServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: AdminRoleServiceImpl
 * @date 2022/9/18 19:33
 */
@DubboService
public class AdminRoleServiceImpl implements AdminRoleService {
    @Resource
    private AdminRoleMapper adminRoleMapper;

    @Override
    public Role selectByAdminId(Long adminId) {
        return adminRoleMapper.selectByAdminId(adminId);
    }

    @Override
    public void save(AdminRole adminRole) {
        adminRoleMapper.save(adminRole);
    }

    @Override
    public void updateByUserId(AdminEditDTO adminEditDTO) {
        if (this.selectByAdminId(adminEditDTO.getAdminId()) == null) {
            AdminRole adminRole = new AdminRole(SnowFlakeUtils.nextId(), adminEditDTO.getAdminId(), adminEditDTO.getRoleId());
            adminRole.setCreateTime(adminEditDTO.getUpdateTime());
            adminRole.setUpdateTime(new Date());
            this.save(adminRole);
            return;
        }
        adminEditDTO.setUpdateTime(new Date());
        adminRoleMapper.updateByAdminId(adminEditDTO);
    }

    @Override
    public boolean existsByRoleId(Long roleId) {
        return adminRoleMapper.selectByRoleId(roleId) > 0;
    }
}
