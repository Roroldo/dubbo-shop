package com.topband.shop.provider.mapper;


import com.topband.shop.entity.UserActive;
import com.topband.shop.view.UserActiveVO;

import java.util.List;

/**
 * @version: V1.0
 * @author: huangyijun
 * @className: UserActiveMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: UserActiveMapper
 * @date: 2022-09-09 15:44
 */
public interface UserActiveMapper {
    void save(UserActive userActive);

    List<UserActiveVO> countActiveByDate(int day);

    List<UserActiveVO> countNewByDate(int day);
}
