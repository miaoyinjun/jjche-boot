package org.jjche.filter.encryption.api;

import cn.hutool.core.collection.CollUtil;
import org.jjche.common.constant.FilterEncryptionConstant;
import org.jjche.filter.encryption.api.interceptor.EncryptionCheckParamInterceptor;
import org.jjche.filter.encryption.api.interceptor.EncryptionMd5Interceptor;
import org.jjche.filter.encryption.api.interceptor.EncryptionRsaInterceptor;
import org.jjche.filter.property.FilterEncryptionProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = FilterEncryptionConstant.PROPERTY_PACKAGE_PREFIX, name = "enabled", havingValue = "true")
@Import({FilterEncryptionProperties.class})
public class FilterEncryptionAutoConfiguration {
    @Autowired
    private FilterEncryptionProperties encryptionProperties;

    /**
     * <p>
     * 过滤器配置
     * </p>
     *
     * @return a {@link org.springframework.web.servlet.config.annotation.WebMvcConfigurer} object.
     * @author miaoyj
     * @since 2020-08-13
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                List<String> filters = encryptionProperties.getFilterUrls();
                InterceptorRegistration checkParamInterceptorRegistration = null;
                InterceptorRegistration interceptorRegistration = null;
                /** 默认拦截项目中定义的接口地址*/
//                if (CollUtil.isEmpty(filters)) {
//                    filters = CollUtil.newArrayList();
//                    try {
//                        String filter = ApiUtil.getApiDefineField();
//                        if (StrUtil.isNotBlank(filter)) {
//                            filter += "/**";
//                            filters.add(filter);
//                        }
//                    } catch (Exception e) {
//
//                    }
//                }
                EncryptionCheckParamInterceptor checkParamInterceptor = new EncryptionCheckParamInterceptor(encryptionProperties);
                checkParamInterceptorRegistration = registry.addInterceptor(checkParamInterceptor);
                /**加密类型MD5|RSA*/
                if (encryptionProperties.getType().equalsIgnoreCase(FilterEncryptionConstant.TYPE_MD5)) {
                    EncryptionMd5Interceptor md5Filter = new EncryptionMd5Interceptor(encryptionProperties);
                    interceptorRegistration = registry.addInterceptor(md5Filter);
                } else {
                    EncryptionRsaInterceptor rsaInterceptor = new EncryptionRsaInterceptor(encryptionProperties);
                    interceptorRegistration = registry.addInterceptor(rsaInterceptor);
                }
                /**添加要过滤的URL*/
                if (CollUtil.isNotEmpty(filters)) {
                    checkParamInterceptorRegistration.addPathPatterns(filters);
                    interceptorRegistration.addPathPatterns(filters);
                }
                /** 排除一些不必要的URL*/
                List<String> excludes = encryptionProperties.getExcludeUrls();
                if (CollUtil.isNotEmpty(excludes)) {
                    checkParamInterceptorRegistration.excludePathPatterns(excludes);
                    interceptorRegistration.excludePathPatterns(excludes);
                }
            }
        };
    }
}
