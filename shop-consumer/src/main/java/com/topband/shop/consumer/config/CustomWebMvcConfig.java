package com.topband.shop.consumer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.topband.shop.api.RoleService;
import com.topband.shop.consumer.interceptor.ClientAuthorizationInterceptor;
import com.topband.shop.consumer.interceptor.GetIpInterceptor;
import com.topband.shop.consumer.interceptor.WebAuthorizationInterceptor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.topband.shop.constants.BusinessConstants.GOODS_IMAGES_PATH;

/**
 * @version: v1.0
 * @author: huangyijun
 * @className: CustomWebMvcConfig
 * @packageName: com.topband.shop.provider.config
 * @description: CustomWebMvcConfig
 * @date 2022/9/8 18:40
 */
@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RoleService roleService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ClientAuthorizationInterceptor(redisTemplate))
                .addPathPatterns(getClientInterceptorList());

        registry.addInterceptor(new WebAuthorizationInterceptor(redisTemplate, roleService))
                .addPathPatterns("/**").excludePathPatterns(getWebExcludedList());

        registry.addInterceptor(new GetIpInterceptor()).addPathPatterns("/**");
    }


    private List<String> getClientInterceptorList() {
        // 客户端拦截需要拦截的，放行其他所有的
        List<String> clientInterceptorList = new ArrayList<>();
        clientInterceptorList.add("/user-voucher/spike/**");
        clientInterceptorList.add("/orders/create/**");
        clientInterceptorList.add("/client/user/exit");
        return clientInterceptorList;
    }

    private List<String> getWebExcludedList() {
        List<String> webExcludedList = new ArrayList<>();
        webExcludedList.add("/verification/code");;
        webExcludedList.add("/swagger-resources/**");
        webExcludedList.add("/v2/*");
        webExcludedList.add("/swagger-ui.html/**");
        webExcludedList.add("/webjars/**");
        webExcludedList.add("/web/admin/login");
        webExcludedList.add("/web/admin/password/sendEmail");
        webExcludedList.add("/web/admin/resetPassword");
        webExcludedList.add("/web/admin/verify/email");
        // 放行客户端的所有接口
        webExcludedList.add("/client/user/login");
        webExcludedList.add("/client/user/register");
        webExcludedList.add("/user-voucher/spike/**");
        webExcludedList.add("/orders/create/**");
        webExcludedList.add("/goods/client/list");
        webExcludedList.add("/voucher/client/list");
        webExcludedList.add("/static/**");
        webExcludedList.add("/client/user/exit");
        webExcludedList.add("/goodsImg/**");
        return webExcludedList;
    }

    @Bean
    public HttpMessageConverters customConverters() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(smt);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        /*
            解决 long 类型前端精度丢失问题
         */
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        // 设置中文编码格式
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return new HttpMessageConverters(mappingJackson2HttpMessageConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/goodsImg/**").addResourceLocations(GOODS_IMAGES_PATH);
    }
}
