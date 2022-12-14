package com.topband.shop.provider.impl;

import com.topband.shop.api.PermissionService;
import com.topband.shop.api.RoleService;
import com.topband.shop.entity.Role;
import com.topband.shop.provider.mapper.PermissionMapper;
import com.topband.shop.view.PermissionVO;
import com.topband.shop.view.RolePermissionVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.topband.shop.constants.RedisConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: PermissionServiceImpl
 * @packageName: com.topband.shop.provider.impl
 * @description: PermissionServiceImpl
 * @date 2022/9/19 14:33
 */
@DubboService
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RoleService roleService;

    @Override
    public List<PermissionVO> listForAdd() {
        List<PermissionVO> permissionVOList = permissionMapper.list(null);
        permissionVOList.forEach(p -> p.setSelected(false));
        return handlePermissionVOListToTree(permissionVOList);
    }


    @Override
    public List<PermissionVO> menuTree(Long roleId) {
        initRolePermissionToRedis(roleId);
        return handlePermissionVOListToTree(permissionMapper.list(roleId));
    }

    @Override
    public RolePermissionVO listByRoleId(Long roleId) {
        Role role = roleService.selectById(roleId);
        if (role == null) {
            return null;
        }
        RolePermissionVO rolePermissionVO = new RolePermissionVO();
        rolePermissionVO.setRoleId(roleId);
        rolePermissionVO.setRoleName(role.getName());
        rolePermissionVO.setPermissions(wrapPermissionVOList(permissionMapper.list(roleId)));
        return rolePermissionVO;
    }

    /**
     * ????????????????????????????????? redis ???
     */
    private void initRolePermissionToRedis(Long roleId) {
        String roleKey = ROLE_PERMISSIONS_SET_KEY + roleId;
        Set<Object> members = redisTemplate.opsForSet().members(roleKey);
        if (members == null || members.size() == 0) {
            List<PermissionVO> rolePermissionVOList = permissionMapper.list(roleId);
            // ??????????????????????????? redis
            rolePermissionVOList.stream()
                    .filter(p -> p.getParentId() != 0)
                    .forEach(p -> redisTemplate.opsForSet().add(roleKey, p.getUrl()));
            redisTemplate.expire(roleKey, ROLE_PERMISSIONS_SET_KEY_EXPIRE, TimeUnit.HOURS);
            log.info("???????????????{} ???????????? redis ...", roleId);
        }
    }

    /**
     * ??????????????????????????????????????????????????????
     */
    @PostConstruct
    public void initSystemPermissionToRedis() {
        Set<Object> needPermissions = redisTemplate.opsForSet().members(NEED_ROLE_PERMISSIONS_SET_KEY);
        if (needPermissions == null || needPermissions.size() == 0) {
            List<PermissionVO> allPermissionVOList = permissionMapper.list(null);
            allPermissionVOList.stream()
                    .filter(p -> p.getParentId() != 0)
                    .forEach(p -> redisTemplate.opsForSet().add(NEED_ROLE_PERMISSIONS_SET_KEY, p.getUrl()));
            log.info("???????????????????????????????????? redis....");
        }
    }

    /**
     * ??????????????????????????????
     */
    private List<PermissionVO> handlePermissionVOListToTree(List<PermissionVO> permissionVOList) {
        List<PermissionVO> treePermissionVOList = new ArrayList<>(permissionVOList.size());
        Map<Long, PermissionVO> permissionMap = new HashMap<>(permissionVOList.size());

        // ???????????? map
        permissionVOList.forEach(permissionVO -> permissionMap.put(permissionVO.getId(), permissionVO));
        for (PermissionVO permissionVO : permissionVOList) {
            // ?????????????????? id ??? 0
            if (permissionVO.getParentId() == 0) {
                treePermissionVOList.add(permissionVO);
            } else {
                // ????????????
                PermissionVO parentPermissionVO = permissionMap.get(permissionVO.getParentId());
                if (parentPermissionVO.getChildren() == null) { // ????????????????????????
                    List<PermissionVO> children = new ArrayList<>();
                    parentPermissionVO.setChildren(children);
                }
                // ????????????????????????
                parentPermissionVO.getChildren().add(permissionVO);
            }
        }
        return treePermissionVOList;
    }

    /**
     * ?????? permissionVOList?????????????????????????????????????????????????????????
     */
    private List<PermissionVO> wrapPermissionVOList(List<PermissionVO> rolePermissionVOList) {
        // ?????????????????????????????????????????????????????????????????????????????????
        List<PermissionVO> allPermissionVOList = permissionMapper.list(null);

        // ????????????????????????????????????
        if (allPermissionVOList.size() == rolePermissionVOList.size()) {
            allPermissionVOList.forEach(permissionVO -> permissionVO.setSelected(true));
        }
        // ???????????? url
        Set<String> rolePermissionUrlsSet = rolePermissionVOList.stream()
                .map(PermissionVO::getUrl).collect(Collectors.toSet());
        // ??????????????????
        allPermissionVOList.forEach(permissionVO -> {
            permissionVO.setSelected(rolePermissionUrlsSet.contains(permissionVO.getUrl()));
        });
        return handlePermissionVOListToTree(allPermissionVOList);
    }
}
