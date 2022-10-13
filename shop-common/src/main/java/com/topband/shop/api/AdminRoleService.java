package com.topband.shop.api;

import com.topband.shop.dto.AdminEditDTO;
import com.topband.shop.entity.AdminRole;
import com.topband.shop.entity.Role;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminRoleService
 * @packageName: com.topband.shop.api
 * @description: AdminRoleService
 * @date 2022/9/18 19:30
 */
public interface AdminRoleService {
    Role selectByAdminId(Long adminId);

    void save(AdminRole adminRole);

    void updateByUserId(AdminEditDTO adminEditDTO);

    boolean existsByRoleId(Long roleId);
}
