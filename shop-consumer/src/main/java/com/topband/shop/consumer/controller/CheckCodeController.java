package com.topband.shop.consumer.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.lang.UUID;
import com.topband.shop.utils.CheckCodeUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.BusinessConstants.CHECK_CODE_COOKIE_NAME;
import static com.topband.shop.constants.RedisConstants.WEB_LOGIN_CHECK_CODE_KEY;
import static com.topband.shop.constants.RedisConstants.WEB_LOGIN_CHECK_CODE_KEY_EXPIRE;


/**
 * @version: v1.0
 * @author: huangyijun
 * @className: CheckCodeController
 * @packageName: com.topband.shop.consumer.controller
 * @description: CheckCodeController
 * @date 2022/9/12 15:24
 */
@Slf4j
@RestController
@RequestMapping("/verification")
@Api(tags = "获取验证码接口")
public class CheckCodeController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @GetMapping(value = "/code", produces = MediaType.IMAGE_JPEG_VALUE)
    public void getLoginCheckCode(HttpServletResponse response) throws IOException {
        // 设置 response 响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 设置图片的宽和高
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(100, 30);
        // 生成验证码
        lineCaptcha.setGenerator(CheckCodeUtils.createLoginCheckCode());
        String checkCodeToken = UUID.randomUUID().toString(true);
        Cookie checkCodeCookie = new Cookie(CHECK_CODE_COOKIE_NAME, checkCodeToken);
        checkCodeCookie.setPath("/");
        // 设置验证码 checkCodeCookie 最大存活时间
        response.addCookie(checkCodeCookie);
        // 关闭浏览器后销毁验证码的 cookie
        checkCodeCookie.setMaxAge(-1);
        log.info("生成的登录验证码是：{}", lineCaptcha.getCode());
        log.info("生成的登录验证码的 cookieValue 是：{}", checkCodeToken);
        redisTemplate.opsForValue().set(WEB_LOGIN_CHECK_CODE_KEY + checkCodeToken, lineCaptcha.getCode(),
                WEB_LOGIN_CHECK_CODE_KEY_EXPIRE, TimeUnit.MINUTES);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            lineCaptcha.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("获取 response 输出流失败，原因：{}", ExceptionUtils.getStackTrace(e));
        } finally {
            assert outputStream != null;
            outputStream.close();
        }
    }
}
