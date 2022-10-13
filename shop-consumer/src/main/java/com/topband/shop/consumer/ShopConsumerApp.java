package com.topband.shop.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ShopConsumerApp
 * @packageName: com.topband.shop.consumer
 * @description: ShopConsumerApp
 * @date 2022/9/8 14:35
 */
@SpringBootApplication
// 扫描 swagger 配置
@ComponentScan(basePackages = "com.topband.shop")
public class ShopConsumerApp {
    public static void main(String[] args) {
        SpringApplication.run(ShopConsumerApp.class, args);
    }
}
