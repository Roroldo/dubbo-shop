package com.topband.shop.api;

import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RolePermissionService
 * @packageName: com.topband.shop.api
 * @description: RolePermissionService
 * @date 2022/9/19 16:43
 */
public interface RolePermissionService {
    void batchSave(Long roleId, List<Long> permissionId);

    void updateByRoleId(Long roleId, List<Long> permissionsId);

    void deleteByRoleId(Long roleId);
}
