package com.topband.shop.provider.impl;

import com.topband.shop.api.UserActiveService;
import com.topband.shop.entity.UserActive;
import com.topband.shop.provider.mapper.UserActiveMapper;
import com.topband.shop.utils.SnowFlakeUtils;
import com.topband.shop.view.UserActiveVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.topband.shop.constants.RedisConstants.USER_ACTIVE_KEY;
import static com.topband.shop.constants.RedisConstants.USER_NEW_KEY;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: UserActiveServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: UserActiveServiceImpl
 * @date 2022/9/9 15:41
 */
@DubboService
@Slf4j
public class UserActiveServiceImpl implements UserActiveService {
    @Resource
    private UserActiveMapper userActiveMapper;

    @Override
    @CacheEvict(cacheNames = USER_ACTIVE_KEY, allEntries = true)
    public void saveActiveUser(UserActive userActive) {
        userActive.setId(SnowFlakeUtils.nextId());
        userActive.setCreateTime(new Date());
        userActive.setUpdateTime(new Date());
        userActiveMapper.save(userActive);
    }

    @Override
    @Cacheable(value = USER_ACTIVE_KEY, key = "#day")
    public List<UserActiveVO> countActiveUserByDate(int day) {
        List<UserActiveVO> realUserActiveVOList = userActiveMapper.countActiveByDate(day);
        return getFilledUserActiveVOList(day, realUserActiveVOList);
    }

    @Override
    @Cacheable(value = USER_NEW_KEY, key = "#day")
    public List<UserActiveVO> countNewUserByDate(int day) {
        List<UserActiveVO> realUserAddVOList = userActiveMapper.countNewByDate(day);
        return getFilledUserActiveVOList(day, realUserAddVOList);
    }

    private List<UserActiveVO> getFilledUserActiveVOList(int day, List<UserActiveVO> realUserActiveVOList) {
        if (realUserActiveVOList.size() == day) {
            return realUserActiveVOList;
        }
        List<UserActiveVO> fillUserActiveVOList = new ArrayList<>(day);
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // ?????????????????????????????????????????????
        for (int i = day; i > 0; i--) {
            calendar.setTime(date);
            calendar.add(Calendar.DATE, - i + 1);
            String dateStr = dateFormat.format(calendar.getTime());
            log.debug("??? {} ????????????????????????{}", i, dateStr);
            // ?????? realUserActiveVOList ???????????????????????????????????????
            Iterator<UserActiveVO> iterator = realUserActiveVOList.iterator();
            boolean isFill = false;
            while (iterator.hasNext()) {
                UserActiveVO realUserActiveVO = iterator.next();
                if (dateStr.equals(realUserActiveVO.getDate())) {
                    realUserActiveVO.setDate(dateStr);
                    fillUserActiveVOList.add(realUserActiveVO);
                    // ???????????????????????????
                    iterator.remove();
                    isFill = true;
                    break;
                }
            }
            if (!isFill) {
                UserActiveVO userActiveVO = new UserActiveVO();
                userActiveVO.setDate(dateStr);
                userActiveVO.setCount(0);
                fillUserActiveVOList.add(userActiveVO);
            }
        }
        return fillUserActiveVOList;
    }
}
