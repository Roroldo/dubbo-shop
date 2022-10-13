package com.topband.shop.consumer.controller;

import com.topband.shop.api.PermissionService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.view.RolePermissionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PermissionController
 * @packageName: com.topband.shop.consumer.controller
 * @description: PermissionController
 * @date 2022/9/19 15:03
 */
@Slf4j
@RestController
@Api(tags = "权限接口")
@RequestMapping("/permission")
public class PermissionController {
    @DubboReference
    private PermissionService permissionService;

    @GetMapping("/role/{roleId}")
    @ApiOperation("根据角色 id 查找角色权限")
    public Result findPermissions(@PathVariable Long roleId) {
        if (roleId == null) {
            return Result.fail();
        }
        RolePermissionVO rolePermissionVO = permissionService.listByRoleId(roleId);
        if (rolePermissionVO == null) {
            return Result.fail(ResultCodeEnum.ROLE_NOT_EXIST);
        }
        return Result.ok(rolePermissionVO);
    }

    @GetMapping("/list")
    @ApiOperation("获取系统可以添加的权限")
    public Result list() {
        return Result.ok(permissionService.listForAdd());
    }
}
