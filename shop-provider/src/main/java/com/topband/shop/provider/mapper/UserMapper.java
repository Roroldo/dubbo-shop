package com.topband.shop.provider.mapper;

import com.topband.shop.entity.User;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserMapper
 * @packageName: com.topband.shop.provider.mapper
 * @description: UserMapper
 * @date 2022/9/8 15:09
 */
public interface UserMapper {
    User selectById(long userId);

    boolean save(User user);

    int countByEmail(String email);

    User selectByEmail(String email);
}
