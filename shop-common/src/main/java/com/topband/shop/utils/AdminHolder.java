package com.topband.shop.utils;


import com.topband.shop.view.AdminVO;

/**
 * AdminHolder
 * @author huangyijun
 */
public class AdminHolder {
    private static final ThreadLocal<AdminVO> ADMINVO_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveAdmin(AdminVO adminVO){
        ADMINVO_THREAD_LOCAL.set(adminVO);
    }

    public static AdminVO getAdmin(){
        return ADMINVO_THREAD_LOCAL.get();
    }

    public static void removeAdmin(){
        ADMINVO_THREAD_LOCAL.remove();
    }
}
