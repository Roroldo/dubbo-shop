package com.topband.shop.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UploadFile
 * @packageName: com.topband.shop.entity
 * @description: UploadFile
 * @date 2022/9/16 19:22
 */
@Data
@ApiModel("上传文件记录视图类")
public class UploadFileVO implements Serializable {
    @ApiModelProperty("上传 id")
    private Long id;

    @ApiModelProperty("上传用户")
    private String adminName;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("上传文件名")
    private String fileName;

    @ApiModelProperty("上传时间")
    private Date uploadTime;
}
