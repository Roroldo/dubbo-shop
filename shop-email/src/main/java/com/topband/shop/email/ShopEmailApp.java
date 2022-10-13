package com.topband.shop.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ShopEmailApp
 * @packageName: com.topband.shop.email
 * @description: ShopEmailApp
 * @date 2022/9/8 17:26
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.topband.shop")
public class ShopEmailApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopEmailApp.class, args);
    }
}
