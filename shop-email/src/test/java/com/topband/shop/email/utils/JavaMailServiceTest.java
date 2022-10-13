package com.topband.shop.email.utils;

import com.topband.shop.email.service.JavaMailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: JavaMailUtilsTest
 * @packageName: com.topband.shop.email.utils
 * @description: JavaMailUtilsTest
 * @date 2022/9/8 17:41
 */
@SpringBootTest
public class JavaMailServiceTest {
    @Resource
    private JavaMailService javaMailService;

    @Test
    public void testSendMail() {
        String to = "807301586@qq.com";
        String code = "1234222";
        String to2 = "807301586@qq.com";
        String code2 = "abcdhda";
        javaMailService.sendMail(to, code);
        javaMailService.sendMail(to2, code2);
    }
}
