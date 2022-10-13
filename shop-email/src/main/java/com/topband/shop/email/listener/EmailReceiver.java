package com.topband.shop.email.listener;

import com.topband.shop.dto.AdminVerifyDTO;
import com.topband.shop.email.service.JavaMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.topband.shop.constants.RabbitMqConstants.EMAIL_QUEUE_NAME;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: DirectReceiver
 * @packageName: com.topband.shop.email.listener
 * @description: 接受并发送邮件
 * @date 2022/9/8 20:16
 */
@Slf4j
@Component
@RabbitListener(queues = EMAIL_QUEUE_NAME)
public class EmailReceiver {
    @Resource
    private JavaMailService javaMailService;

    @RabbitHandler
    public void process(@Payload AdminVerifyDTO adminVerifyDTO) {
        log.info("mq 收到的待发送邮件信息：{}", adminVerifyDTO);
        javaMailService.sendMail(adminVerifyDTO.getEmail(), adminVerifyDTO.getCheckCode());
    }
}