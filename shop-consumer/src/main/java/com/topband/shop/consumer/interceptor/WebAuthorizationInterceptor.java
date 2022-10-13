package com.topband.shop.consumer.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.topband.shop.api.RoleService;
import com.topband.shop.base.Result;
import com.topband.shop.base.ResultCodeEnum;
import com.topband.shop.consumer.anno.root.IsAdmin;
import com.topband.shop.entity.Role;
import com.topband.shop.utils.AdminHolder;
import com.topband.shop.view.AdminVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static com.topband.shop.constants.BusinessConstants.ROOT_ROLE;
import static com.topband.shop.constants.RedisConstants.*;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: WebAuthorizationInterceptor
 * @packageName: com.topband.shop.consumer.interceptor
 * @description: web 端 token 拦截器
 * @date 2022/9/9 16:52
 */
@Slf4j
public class WebAuthorizationInterceptor implements HandlerInterceptor {
    private RedisTemplate<String, Object> redisTemplate;
    private RoleService roleService;

    public WebAuthorizationInterceptor(RedisTemplate<String, Object> redisTemplate, RoleService roleService) {
        this.redisTemplate = redisTemplate;
        this.roleService = roleService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (StrUtil.isBlank(token)) {
            writeResponse(response, Result.fail(ResultCodeEnum.NOT_LOGIN_ERROR));
            return false;
        }
        String adminTokenKey = WEB_TOKEN_KEY + token;
        Map<Object, Object> adminVOMap = redisTemplate.opsForHash().entries(adminTokenKey);
        if (adminVOMap == null || adminVOMap.size() == 0) {
            writeResponse(response, Result.fail(ResultCodeEnum.NOT_LOGIN_ERROR));
            return false;
        }
        AdminVO adminVO = BeanUtil.fillBeanWithMap(adminVOMap, new AdminVO(), false);
        AdminHolder.saveAdmin(adminVO);
        return checkPermission(request, response, handler, adminVO);
    }

    private boolean checkPermission(HttpServletRequest request, HttpServletResponse response, Object handler, AdminVO adminVO) throws IOException {
        // 校验专属于超管的权限
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取方法上面的注解
            IsAdmin isAdmin = handlerMethod.getMethodAnnotation(IsAdmin.class);
            // 校验超管身份
            if (isAdmin != null && !ROOT_ROLE.equals(adminVO.getRoleName())) {
                writeResponse(response, Result.fail(ResultCodeEnum.PERMISSION_REJECT));
                return false;
            }
        }
        // 校验普通权限
        Role role = roleService.selectByName(adminVO.getRoleName());
        Long roleId = role.getId();
        String rolePermissionKey = ROLE_PERMISSIONS_SET_KEY + roleId;
        // 获取请求路径
        String servletPath = request.getServletPath();
        if (isIllegalPermissionUrl(servletPath, rolePermissionKey)) {
            writeResponse(response, Result.fail(ResultCodeEnum.PERMISSION_REJECT));
            return false;
        }
        return true;
    }


    private void writeResponse(HttpServletResponse response, Result result) throws IOException {
        JSONConfig jsonConfig = JSONConfig.create().setIgnoreNullValue(false);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONUtil.toJsonStr(result, jsonConfig));
    }


    private boolean isIllegalPermissionUrl(String servletPath, String rolePermissionKey) {
        servletPath = servletPath.split("\\d")[0];
        log.info("拦截路径：{}", servletPath);
        SetOperations<String, Object> stringObjectSetOperations = redisTemplate.opsForSet();
        return Boolean.TRUE.equals(stringObjectSetOperations.isMember(NEED_ROLE_PERMISSIONS_SET_KEY, servletPath))
                && Boolean.FALSE.equals(stringObjectSetOperations.isMember(rolePermissionKey, servletPath));
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.debug("移除线程上下文保存的用户：{}", AdminHolder.getAdmin());
        AdminHolder.removeAdmin();
    }
}
