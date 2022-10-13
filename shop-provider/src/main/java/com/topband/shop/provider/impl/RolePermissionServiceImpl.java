package com.topband.shop.provider.impl;

import com.topband.shop.api.RolePermissionService;
import com.topband.shop.entity.RolePermission;
import com.topband.shop.provider.mapper.RolePermissionMapper;
import com.topband.shop.utils.SnowFlakeUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RolePermissionServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: RolePermissionServiceImpl
 * @date 2022/9/19 16:45
 */
@DubboService
public class RolePermissionServiceImpl implements RolePermissionService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public void batchSave(Long roleId, List<Long> permissionIds) {
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission(SnowFlakeUtils.nextId(), roleId, permissionId);
            rolePermission.setCreateTime(new Date());
            rolePermission.setUpdateTime(new Date());
            rolePermissionList.add(rolePermission);
        }
        rolePermissionMapper.saveBatch(rolePermissionList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByRoleId(Long roleId, List<Long> permissionsId) {
        // 删除此角色具有的权限
        rolePermissionMapper.deleteByRoleId(roleId, new Date());
        // 重新添加
        batchSave(roleId, permissionsId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        rolePermissionMapper.deleteByRoleId(roleId, new Date());
    }
}
