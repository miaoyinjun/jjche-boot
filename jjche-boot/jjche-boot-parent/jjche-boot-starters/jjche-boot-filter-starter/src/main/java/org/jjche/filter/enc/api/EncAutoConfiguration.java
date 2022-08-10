package org.jjche.filter.enc.api;

import cn.hutool.core.collection.CollUtil;
import org.jjche.cache.service.RedisService;
import org.jjche.common.api.CommonAPI;
import org.jjche.filter.property.FilterEncProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 过滤器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-13
 */
@Configuration
@Import(FilterEncProperties.class)
public class EncAutoConfiguration {
    @Autowired
    private FilterEncProperties filterEncProperties;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CommonAPI commonAPI;

    /**
     * <p>
     * 过滤器配置
     * </p>
     *
     * @return a {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer} object.
     * @author miaoyj
     * @since 2020-08-13
     */
    @Bean("FilterEncWebMvcConfigure")
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                List<String> filterUrls = filterEncProperties.getUrls();
                InterceptorRegistration checkHeaderInterceptorRegistration = null;
                /** 默认拦截项目中定义的接口地址*/
                EncCheckHeaderInterceptor checkParamInterceptor = new EncCheckHeaderInterceptor(commonAPI, redisService);
                checkHeaderInterceptorRegistration = registry.addInterceptor(checkParamInterceptor);
                /**添加要过滤的URL*/
                if (CollUtil.isNotEmpty(filterUrls)) {
                    checkHeaderInterceptorRegistration.addPathPatterns(filterUrls);
                }
            }
        };
    }
}