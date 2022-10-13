package com.topband.shop.consumer.service;

import com.topband.shop.base.Result;

public interface MqEmailSendService {
    Result sendEmail(String email);
}
