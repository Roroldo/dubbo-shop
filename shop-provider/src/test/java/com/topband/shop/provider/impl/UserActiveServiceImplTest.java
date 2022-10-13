package com.topband.shop.provider.impl;

import com.topband.shop.api.UserActiveService;
import com.topband.shop.api.UserService;
import com.topband.shop.dto.UserLoginRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: LocalDateTest
 * @packageName: com.topband.shop.provider.impl
 * @description: UserActiveMapper
 * @date 2022/9/10 16:20
 */
@SpringBootTest
@Slf4j
public class UserActiveServiceImplTest {
    @Resource
    private UserActiveService userActiveService;

    @Resource
    private UserService userService;

    @Test
    public void testLocalDate() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -0);
        log.info("{}", dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.DATE, -6);
        log.info("{}", dateFormat.format(calendar.getTime()));
    }

    @Test
    public void testCountActiveUserByDate() {
        userActiveService.countActiveUserByDate(15).forEach(System.out::println);
    }

    @Test
    public void testCountNewUserByDate(){
        userActiveService.countNewUserByDate(15).forEach(System.out::println);
    }

    @Test
    public void testClearCache(){
        UserLoginRegisterDTO userLoginRegisterDTO = new UserLoginRegisterDTO();
        userLoginRegisterDTO.setEmail("abcd@qq.com");
        userLoginRegisterDTO.setEmail("abcd@qq.com");
        Assertions.assertTrue(userService.register(userLoginRegisterDTO));
    }
}
