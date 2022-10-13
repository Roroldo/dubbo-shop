package com.topband.shop.consumer.controller;

import com.topband.shop.api.RoleService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.config.CustomValidatedGroup;
import com.topband.shop.consumer.anno.root.IsAdmin;
import com.topband.shop.dto.RoleQueryDTO;
import com.topband.shop.utils.AdminHolder;
import com.topband.shop.view.RolePermissionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RoleController
 * @packageName: com.topband.shop.consumer.controller
 * @description: RoleController
 * @date 2022/9/19 10:30
 */
@Slf4j
@RestController
@Api(tags = "角色接口")
@RequestMapping("/role")
public class RoleController {
    @DubboReference
    private RoleService roleService;

    @GetMapping("/roleToAdd")
    @ApiOperation("查询可以添加的角色列表")
    public Result listToAdd() {
        return Result.ok(roleService.roleListForAdd());
    }

    @PostMapping("/list")
    @ApiOperation("分页按条件查询角色")
    public Result list(@RequestBody RoleQueryDTO roleQueryDTO) {
        return Result.ok(roleService.list(roleQueryDTO));
    }

    @DeleteMapping("/delete/{roleId}")
    @ApiOperation("根据 id 删除角色")
    @IsAdmin
    public Result delete(@PathVariable("roleId") Long roleId) {
        if (roleId == null) {
            return Result.fail();
        }
        if (roleService.selectById(roleId) == null) {
            return Result.fail(ResultCodeEnum.ROLE_NOT_EXIST);
        }
        boolean flag = roleService.deleteById(roleId);
        if (flag) {
            return Result.ok();
        }
        return Result.fail(ResultCodeEnum.ROLE_HAS_ADMIN);
    }

    @PostMapping("/add")
    @ApiOperation("添加角色")
    public Result add(@RequestBody @Validated(CustomValidatedGroup.Crud.Create.class)
                              RolePermissionVO rolePermissionVO) {
        Long adminId = AdminHolder.getAdmin().getId();
        return roleService.add(adminId, rolePermissionVO);
    }

    @PutMapping("/update")
    @ApiOperation("根据角色 id 更新角色信息")
    @IsAdmin
    public Result updateById(@RequestBody @Validated(CustomValidatedGroup.Crud.Update.class)
                                     RolePermissionVO rolePermissionVO) {
        return roleService.updateById(rolePermissionVO);
    }
}
