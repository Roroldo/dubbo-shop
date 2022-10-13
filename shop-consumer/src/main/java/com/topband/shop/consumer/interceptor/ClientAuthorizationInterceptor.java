package com.topband.shop.consumer.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.view.UserVO;
import com.topband.shop.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.topband.shop.constants.RedisConstants.CLIENT_TOKEN_KEY;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: ClientAuthorizationInterceptor
 * @packageName: com.topband.shop.consumer.interceptor
 * @description: c 端 token 拦截器
 * @date 2022/9/9 16:52
 */
@Slf4j
public class ClientAuthorizationInterceptor implements HandlerInterceptor {
    private RedisTemplate<String, Object> redisTemplate;

    public ClientAuthorizationInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            handleNotLoginError(response);
            return false;
        }
        String userKey = CLIENT_TOKEN_KEY + token;
        UserVO userVO = (UserVO) redisTemplate.opsForValue().get(userKey);
        if (userVO == null) {
            handleNotLoginError(response);
            return false;
        }
        UserHolder.saveUser(userVO);
        return true;
    }

    private void handleNotLoginError(HttpServletResponse response) throws IOException {
        JSONConfig jsonConfig = JSONConfig.create().setIgnoreNullValue(false);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(Result.fail(ResultCodeEnum.NOT_LOGIN_ERROR), jsonConfig));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("移除线程上下文保存的用户：{}", UserHolder.getUser());
        UserHolder.removeUser();
    }
}
