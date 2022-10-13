package com.topband.shop.provider.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.AdminRoleService;
import com.topband.shop.api.RolePermissionService;
import com.topband.shop.api.RoleService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.RoleQueryDTO;
import com.topband.shop.entity.Role;
import com.topband.shop.provider.mapper.RoleMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.RolePermissionVO;
import com.topband.shop.view.RoleVO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.topband.shop.constants.RedisConstants.ROLE_PERMISSIONS_SET_KEY;
import static org.apache.catalina.util.ContextName.ROOT_NAME;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: RoleServiceImpl
 * @date 2022/9/12 20:36
 */
@DubboService
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RolePermissionService rolePermissionService;

    @Override
    public Role selectById(long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public List<Role> roleListForAdd() {
        return roleMapper.selectRoleForAdd();
    }

    @Override
    public ResultPage list(RoleQueryDTO roleQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(roleQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(roleQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<RoleVO> roleVOList = roleMapper.list(roleQueryDTO);
        PageInfo<RoleVO> pageInfo = new PageInfo<>(roleVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long roleId) {
        if (adminRoleService.existsByRoleId(roleId)) {
            return false;
        }
        Role role = roleMapper.selectById(roleId);
        if (role.getName().equals(ROOT_NAME)) {
            return false;
        }
        roleMapper.deleteById(roleId, new Date());
        // 删除角色的权限
        rolePermissionService.deleteByRoleId(roleId);
        // 删除缓存
        redisTemplate.delete(ROLE_PERMISSIONS_SET_KEY + roleId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(Long adminId, RolePermissionVO rolePermissionVO) {
        String roleName = rolePermissionVO.getRoleName();
        List<Long> permissionsId = rolePermissionVO.getPermissionsId();
        if (isInvalidPermissionIds(permissionsId)) {
            return Result.fail();
        }
        if (roleMapper.countByRoleName(roleName) > 0) {
            return Result.fail(ResultCodeEnum.ROLE_NAME_REPEAT_ERROR);
        }
        Role role = new Role();
        role.setId(SnowFlakeUtils.nextId());
        role.setCreateAdminId(adminId);
        role.setName(roleName);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        roleMapper.save(role);
        rolePermissionService.batchSave(role.getId(), permissionsId);
        return Result.ok();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateById(RolePermissionVO rolePermissionVO) {
        String roleName = rolePermissionVO.getRoleName();

        List<Long> permissionsId = rolePermissionVO.getPermissionsId();
        // 权限列表为空
        if (isInvalidPermissionIds(permissionsId)) {
            return Result.fail();
        }
        Long roleId = rolePermissionVO.getRoleId();
        // 如果修改的是超管
        Role target = roleMapper.selectById(roleId);
        if (target.getName().equals(ROOT_NAME)) {
            return Result.fail();
        }
        // 重复角色名
        if (roleMapper.countByRepeatName(roleName, roleId) > 0) {
            return Result.fail(ResultCodeEnum.ROLE_NAME_REPEAT_ERROR);
        }
        // 更新角色信息
        Role role = new Role();
        role.setId(roleId);
        role.setName(roleName);
        role.setUpdateTime(new Date());
        roleMapper.updateById(role);
        // 更新权限列表
        rolePermissionService.updateByRoleId(role.getId(), permissionsId);
        // 删除缓存
        redisTemplate.delete(ROLE_PERMISSIONS_SET_KEY + roleId);
        return Result.ok();
    }

    @Override
    public Role selectByName(String roleName) {
        return roleMapper.selectByName(roleName);
    }

    private boolean isInvalidPermissionIds(List<Long> permissionsId) {
        permissionsId.removeIf(Objects::isNull);
        return permissionsId.size() == 0;
    }
}
