package com.topband.shop.view;

import com.topband.shop.config.CustomValidatedGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PermissionVO
 * @packageName: com.topband.shop.view
 * @description: PermissionVO
 * @date 2022/9/19 14:21
 */
@Data
@ApiModel("角色权限视图类")
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissionVO implements Serializable {
    @ApiModelProperty(value = "角色 id")
    @NotNull(message = "修改的角色 id 不能为 null", groups = CustomValidatedGroup.Crud.Update.class)
    private Long roleId;

    @ApiModelProperty("角色名")
    @NotNull(groups = {CustomValidatedGroup.Crud.Create.class, CustomValidatedGroup.Crud.Update.class},
            message = "请输入1-50个字符")
    @Length(min = 1, max = 50,
            groups = {CustomValidatedGroup.Crud.Create.class, CustomValidatedGroup.Crud.Update.class},
            message = "请输入1-50个字符")
    private String roleName;

    @ApiModelProperty(hidden = true)
    private List<PermissionVO> permissions;

    @NotNull(message = "修改的权限列表 id 不能为 null",
            groups = {CustomValidatedGroup.Crud.Create.class, CustomValidatedGroup.Crud.Update.class})
    private List<Long> permissionsId;
}
