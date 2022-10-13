package com.topband.shop.api;

import com.topband.shop.base.Result;
import com.topband.shop.dto.UserLoginRegisterDTO;

public interface UserService {
    boolean register(UserLoginRegisterDTO userRegisterDTO);
    boolean isRepeatEmail(String email);
    Result login(UserLoginRegisterDTO userLoginDTO);
    Result exit(String token);
}
