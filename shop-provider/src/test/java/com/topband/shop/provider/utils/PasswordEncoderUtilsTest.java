package com.topband.shop.provider.utils;

import com.topband.shop.utils.PasswordEncoderUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PasswordEncoderUtilsTest
 * @packageName: com.topband.shop.provider.utils
 * @description: PasswordEncoderUtilsTest
 * @date 2022/9/9 8:57
 */
@SpringBootTest
public class PasswordEncoderUtilsTest {

    @Test
    public void testEncodePassword() {
        String password = "Aa123456";
        String encode = PasswordEncoderUtils.encode(password);
        Assertions.assertTrue(PasswordEncoderUtils.matches(encode, password));
    }
}
