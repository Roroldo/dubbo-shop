package com.topband.shop.api;

import com.topband.shop.view.PermissionVO;
import com.topband.shop.view.RolePermissionVO;

import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PermissionService
 * @packageName: com.topband.shop.api
 * @description: PermissionService
 * @date 2022/9/19 14:32
 */
public interface PermissionService {
    /**
     * 返回系统中的所有可以添加的权限
     */
    List<PermissionVO> listForAdd();

    /**
     * 菜单权限
     */
    List<PermissionVO> menuTree(Long roleId);


    /**
     * 根据 roleId 返回角色和其权限，如果当前登录用户不是超管，需要过滤掉超管的专有权限
     */
    RolePermissionVO listByRoleId(Long roleId);
}
