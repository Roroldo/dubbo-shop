package com.topband.shop.provider.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.topband.shop.api.UserActiveService;
import com.topband.shop.api.UserService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.dto.UserLoginRegisterDTO;
import com.topband.shop.entity.User;
import com.topband.shop.entity.UserActive;
import com.topband.shop.provider.mapper.UserMapper;
import com.topband.shop.utils.PasswordEncoderUtils;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.topband.shop.constants.RedisConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: UserServiceImpl
 * @date 2022/9/8 15:12
 */
@DubboService
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserActiveService userActiveService;


    @Override
    @CacheEvict(cacheNames = USER_NEW_KEY, allEntries = true)
    public boolean register(UserLoginRegisterDTO userRegisterDTO) {
        String email = userRegisterDTO.getEmail();
        String password = userRegisterDTO.getPassword();
        // 判断邮箱是否被使用
        if (isRepeatEmail(email)) {
            log.info("邮箱：{} 已被使用", email);
            return false;
        }
        User user = BeanUtil.copyProperties(userRegisterDTO, User.class);
        user.setId(SnowFlakeUtils.nextId());
        user.setPassword(PasswordEncoderUtils.encode(password));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        return userMapper.save(user);
    }

    @Override
    public boolean isRepeatEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public Result login(UserLoginRegisterDTO userLoginRegisterDTO) {
        // 根据邮件查找用户
        User user = userMapper.selectByEmail(userLoginRegisterDTO.getEmail());
        if (user != null) {
            // 密码校验正确
            if (PasswordEncoderUtils.matches(user.getPassword(), userLoginRegisterDTO.getPassword())) {
                // 先判断另一个地方的 token 存不存在，存在要先删除原来的 token（踢人下线）
                String loginKey = CLIENT_LOGIN_KEY + user.getId();
                String oldToken = (String) redisTemplate.opsForValue().get(loginKey);
                if (oldToken != null) {
                    redisTemplate.delete(CLIENT_TOKEN_KEY + oldToken);
                }
                // 登录
                String token = UUID.randomUUID().toString(true);
                UserVO userVO =  BeanUtil.copyProperties(user, UserVO.class);
                redisTemplate.opsForValue().set(CLIENT_TOKEN_KEY + token,
                        userVO, TOKEN_EXPIRE, TimeUnit.HOURS);
                // 再存一份，踢人下线使用
                redisTemplate.opsForValue().set(loginKey,
                        token, TOKEN_EXPIRE, TimeUnit.HOURS);
                // 记录用户信息到活跃表
                UserActive userActive = new UserActive(user.getId(), new Date());
                userActiveService.saveActiveUser(userActive);
                Map<String, Object> map = new HashMap<>(2);
                map.put("token", token);
                map.put("userVO", userVO);
                return Result.ok(map);
            }
        }
        return Result.fail(ResultCodeEnum.LOGIN_FAIL);
    }

    @Override
    public Result exit(String token) {
        UserVO userVO = (UserVO) redisTemplate.opsForValue().get(CLIENT_TOKEN_KEY + token);
        if (userVO != null) {
            // 删除登录凭证
            redisTemplate.delete(CLIENT_TOKEN_KEY + token);
            // 删除踢人下线的副本
            redisTemplate.delete(CLIENT_LOGIN_KEY + userVO.getId());
        }
        return Result.ok();
    }
}
