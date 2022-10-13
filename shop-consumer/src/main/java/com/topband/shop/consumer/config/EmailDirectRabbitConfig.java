package com.topband.shop.consumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.topband.shop.constants.RabbitMqConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: DirectRabbitConfig
 * @packageName: com.topband.shop.email.config
 * @description: EmailDirectRabbitConfig
 * @date 2022/9/8 20:10
 */
@Configuration
public class EmailDirectRabbitConfig {
    @Bean
    public Queue emailDirectQueue() {
        return new Queue(EMAIL_QUEUE_NAME, true);
    }

    @Bean
    DirectExchange emailDirectExchange() {
        return new DirectExchange(EMAIL_DIRECT_EXCHANGE_NAME);
    }

    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(emailDirectQueue()).to(emailDirectExchange()).with(EMAIL_EXCHANGE_ROUTING_KEY);
    }
}
