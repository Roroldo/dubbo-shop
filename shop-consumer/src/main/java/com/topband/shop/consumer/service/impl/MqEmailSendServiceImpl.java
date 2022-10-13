package com.topband.shop.consumer.service.impl;

import com.topband.shop.base.Result;
import com.topband.shop.consumer.service.MqEmailSendService;
import com.topband.shop.dto.AdminVerifyDTO;
import com.topband.shop.utils.CheckCodeUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.RabbitMqConstants.EMAIL_DIRECT_EXCHANGE_NAME;
import static com.topband.shop.constants.RabbitMqConstants.EMAIL_EXCHANGE_ROUTING_KEY;
import static com.topband.shop.constants.RedisConstants.EMAIL_CHECK_CODE_EXPIRE;
import static com.topband.shop.constants.RedisConstants.EMAIL_CHECK_CODE_KEY;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: RabbitMqServiceImpl
 * @packageName: com.topband.shop.consumer.service.impl
 * @description: RabbitMqServiceImpl
 * @date 2022/9/14 17:08
 */
@Service
public class MqEmailSendServiceImpl implements MqEmailSendService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Result sendEmail(String email) {
        String emailCheckCode = CheckCodeUtils.createEmailCheckCode();
        redisTemplate.opsForValue().set(EMAIL_CHECK_CODE_KEY + email, emailCheckCode, EMAIL_CHECK_CODE_EXPIRE, TimeUnit.MINUTES);
        AdminVerifyDTO adminVerifyDTO = new AdminVerifyDTO(email, emailCheckCode);
        rabbitTemplate.convertAndSend(EMAIL_DIRECT_EXCHANGE_NAME, EMAIL_EXCHANGE_ROUTING_KEY, adminVerifyDTO);
        return Result.ok();
    }
}
