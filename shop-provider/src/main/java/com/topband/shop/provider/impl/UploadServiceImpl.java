package com.topband.shop.provider.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.topband.shop.api.UploadFileService;
import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.UploadFile;
import com.topband.shop.provider.mapper.UploadFileMapper;
import com.topband.shop.utils.CheckPageUtils;
import com.topband.shop.view.UploadFileVO;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UploadServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: UploadServiceImpl
 * @date 2022/9/17 14:42
 */
@DubboService
public class UploadServiceImpl implements UploadFileService {
    @Resource
    private UploadFileMapper uploadFileMapper;

    @Override
    public void save(UploadFile uploadFile) {
        uploadFile.setCreateTime(new Date());
        uploadFile.setUpdateTime(new Date());
        uploadFileMapper.save(uploadFile);
    }

    @Override
    public void updateById(UploadFile uploadFile) {
        uploadFile.setUpdateTime(new Date());
        uploadFileMapper.updateById(uploadFile);
    }

    @Override
    public ResultPage list(PageQueryDTO pageQueryDTO) {
        int currentPage = CheckPageUtils.getCurrentPage(pageQueryDTO.getCurrentPage());
        int pageSize = CheckPageUtils.getPageSize(pageQueryDTO.getPageSize());
        PageHelper.startPage(currentPage, pageSize);
        List<UploadFileVO> uploadFileVOList = uploadFileMapper.list(pageQueryDTO.getStart(), pageQueryDTO.getEnd());
        PageInfo<UploadFileVO> pageInfo = new PageInfo<>(uploadFileVOList);
        return ResultPage.build(pageInfo.getTotal(), pageInfo.getList());
    }
}
