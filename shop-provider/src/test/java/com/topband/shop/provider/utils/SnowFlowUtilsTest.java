package com.topband.shop.provider.utils;

import com.topband.shop.utils.SnowFlakeUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: SnowFlowUtilsTest
 * @packageName: com.topband.shop.provider.utils
 * @description: SnowFlowUtilsTest
 * @date 2022/9/19 14:00
 */
@SpringBootTest
@Slf4j
public class SnowFlowUtilsTest {

    @Test
    public void testNextId() {
        for (int i = 0; i < 20; i++) {
            log.info(String.valueOf(SnowFlakeUtils.nextId()));
        }
    }

    @Test
    public void testPrintTreeDir() {
        File file = new File("E:\\myCode\\huangyijun\\dubbo-shop-hyj\\shop-provider");
        // 深度遍历
        printTree(file, 0);
    }

    private void printTree(File file, int level) {
        if (file.exists()) {
            if (level == 0) {
                System.out.println(file.getName());
            } else {
                for (int i = 0; i < level; i++) {
                    System.out.print(" ");
                }
                System.out.println("├" + file.getName());
            }
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                level++;
                for (File childFile : files) {
                    printTree(childFile, level);
                }
            }
        }
    }
}
