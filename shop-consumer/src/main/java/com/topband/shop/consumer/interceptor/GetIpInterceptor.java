package com.topband.shop.consumer.interceptor;

import com.topband.shop.utils.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: GetIpInterceptor
 * @packageName: com.topband.four.interceptor
 * @description: 获取 ip 的拦截器
 * @date 2022/8/25 13:24
 */
@Slf4j
// 执行顺序：监听器 => 过滤器=> 拦截器=> AOP
public class GetIpInterceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行调用（Controller方法调用之前）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("客户端 ip 地址：{}", getRemoteIp(request));
        RequestContext.setRemoteAddr(getRemoteIp(request));
        return true;
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后），不一定会触发
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        RequestContext.removeRemoteAddr();
    }

    /**
     * 在整个请求结束之后被调用，也就是在 DispatcherServlet 渲染了对应的视图之后执行
     * （主要是用于进行资源清理工作），肯定会触发
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.debug("GetIpInterceptor afterCompletion");
    }

    /**
     * 使用nginx做反向代理，需要用该方法才能取到真实的远程IP
     */
    public String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
