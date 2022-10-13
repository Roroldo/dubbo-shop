package com.topband.shop.view;

import com.topband.shop.config.CustomValidatedGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminVO
 * @packageName: com.topband.shop.view
 * @description: AdminVO
 * @date 2022/9/12 20:08
 */
@Data
@ApiModel("管理员视图实体类")
public class AdminVO implements Serializable {
    @ApiModelProperty("管理员 id")
    private Long id;

    @ApiModelProperty("管理员名")
    @NotNull(message = "请输入1-50个字符", groups = {CustomValidatedGroup.Crud.Update.class})
    @Length(min = 1, max = 50, message = "请输入1-50个字符", groups = {CustomValidatedGroup.Crud.Update.class})
    private String name;

    @ApiModelProperty("管理员邮箱")
    private String email;

    @ApiModelProperty("账号状态")
    private String status;

    @ApiModelProperty("角色")
    private String roleName;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
