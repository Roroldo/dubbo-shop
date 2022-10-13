package com.topband.shop.api;

import com.topband.shop.base.ResultPage;
import com.topband.shop.dto.PageQueryDTO;
import com.topband.shop.entity.UploadFile;

public interface UploadFileService {
    void save(UploadFile uploadFile);
    void updateById(UploadFile uploadFile);
    ResultPage list(PageQueryDTO pageQueryDTO);
}
