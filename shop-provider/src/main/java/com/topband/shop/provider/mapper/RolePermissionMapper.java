package com.topband.shop.provider.mapper;

import com.topband.shop.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RolePermissionMapper {
    void saveBatch(List<RolePermission> rolePermissionList);

    void deleteByRoleId(@Param("roleId") Long roleId, @Param("updateTime")Date updateTime);
}
