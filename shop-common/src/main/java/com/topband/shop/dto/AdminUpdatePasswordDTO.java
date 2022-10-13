package com.topband.shop.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: AdminUpdatePasswordDTO
 * @packageName: com.topband.shop.dto
 * @description: AdminUpdatePasswordDTO
 * @date 2022/9/14 20:03
 */
@ApiModel("更新密码表单")
@Data
public class AdminUpdatePasswordDTO implements Serializable {
    @ApiModelProperty(value = "管理员主键 id", hidden = true)
    private Long adminId;

    @ApiModelProperty("旧密码")
    @NotBlank
    private String oldPassword;

    @ApiModelProperty("新密码")
    @NotNull
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$", message = "新密码由8-20位的数字，大小写字母、特殊符号3种以上组成")
    private String newPassword;

    @ApiModelProperty("确认新密码")
    @NotNull
    @Pattern(regexp = "^[^\\u4e00-\\u9fa5 ]{8,20}$", message = "新密码由8-20位的数字，大小写字母、特殊符号3种以上组成")
    private String confirmNewPassword;
}
