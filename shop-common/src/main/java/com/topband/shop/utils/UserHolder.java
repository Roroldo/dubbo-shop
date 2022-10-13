package com.topband.shop.utils;


import com.topband.shop.view.UserVO;

/**
 * UserHolder
 * @author huangyijun
 */
public class UserHolder {
    private static final ThreadLocal<UserVO> USER_DTO_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUser(UserVO user){
        USER_DTO_THREAD_LOCAL.set(user);
    }

    public static UserVO getUser(){
        return USER_DTO_THREAD_LOCAL.get();
    }

    public static void removeUser(){
        USER_DTO_THREAD_LOCAL.remove();
    }
}
