package com.topband.shop.api;

import com.topband.shop.entity.UserActive;
import com.topband.shop.view.UserActiveVO;

import java.util.List;

public interface UserActiveService {
    void saveActiveUser(UserActive userActive);

    List<UserActiveVO> countActiveUserByDate(int day);

    List<UserActiveVO> countNewUserByDate(int day);
}
