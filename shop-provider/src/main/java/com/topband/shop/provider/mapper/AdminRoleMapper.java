package com.topband.shop.provider.mapper;

import com.topband.shop.dto.AdminEditDTO;
import com.topband.shop.entity.AdminRole;
import com.topband.shop.entity.Role;

public interface AdminRoleMapper {
    Role selectByAdminId(Long adminId);

    void save(AdminRole adminRole);

    void updateByAdminId(AdminEditDTO adminEditDTO);

    int selectByRoleId(Long roleId);
}
