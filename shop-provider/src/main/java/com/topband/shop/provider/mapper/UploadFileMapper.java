package com.topband.shop.provider.mapper;

import com.topband.shop.entity.UploadFile;
import com.topband.shop.view.UploadFileVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UploadFileMapper {
    void save(UploadFile uploadFile);
    void updateById(UploadFile uploadFile);
    List<UploadFileVO> list(@Param("start")Date start, @Param("end") Date end);
}
