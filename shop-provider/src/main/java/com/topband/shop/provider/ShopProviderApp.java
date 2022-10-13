package com.topband.shop.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ShopProviderApp
 * @packageName: com.topband.shop.provider
 * @description: ShopProviderApp
 * @date 2022/9/8 15:09
 */
@SpringBootApplication
@EnableDubbo
@MapperScan("com.topband.shop.provider.mapper.**")
@ComponentScan(basePackages = "com.topband.shop")
@EnableCaching
@EnableAspectJAutoProxy(exposeProxy = true)
public class ShopProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopProviderApp.class, args);
    }
}
