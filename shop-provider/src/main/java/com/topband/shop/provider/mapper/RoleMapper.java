package com.topband.shop.provider.mapper;

import com.topband.shop.dto.RoleQueryDTO;
import com.topband.shop.entity.Role;
import com.topband.shop.view.RoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: RoleMapper
 * @date 2022/9/12 20:30
 */
public interface RoleMapper {
    Role selectById(Long roleId);

    List<Role> selectRoleForAdd();

    List<RoleVO> list(RoleQueryDTO roleQueryDTO);

    void deleteById(@Param("roleId") Long roleId, @Param("updateTime") Date updateTime);

    int countByRoleName(String roleName);

    void save(Role role);

    int countByRepeatName(@Param("roleName") String roleName, @Param("roleId") Long roleId);

    void updateById(Role role);

    Role selectByName(String roleName);
}
