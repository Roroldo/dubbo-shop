package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.RoleQueryDTO;
import com.topband.shop.entity.Role;
import com.topband.shop.view.RolePermissionVO;

import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleService
 * @packageName: com.topband.shop.api
 * @description: RoleService
 * @date 2022/9/12 20:33
 */
public interface RoleService {
    Role selectById(long roleId);

    List<Role> roleListForAdd();

    ResultPage list(RoleQueryDTO roleQueryDTO);

    boolean deleteById(Long roleId);

    Result add(Long adminId, RolePermissionVO rolePermissionVO);

    Result updateById(RolePermissionVO rolePermissionVO);

    Role selectByName(String roleName);
}
