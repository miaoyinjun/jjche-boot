package com.boot.admin.core;

import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.annotation.controller.ApiRestController;
import com.boot.admin.core.exception.GlobalExceptionHandler;
import com.boot.admin.core.exception.RequestWrapperFilter;
import com.boot.admin.core.property.CoreApiPathProperties;
import com.boot.admin.core.property.CoreProperties;
import com.boot.admin.core.alarm.dd.AlarmDingTalkService;
import com.boot.admin.core.convert.OriKaMapper;
import com.boot.admin.core.util.SpringContextHolder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({GlobalExceptionHandler.class, OriKaMapper.class,
        CoreProperties.class, cn.hutool.extra.spring.SpringUtil.class,
        RequestWrapperFilter.class, AlarmDingTalkService.class})
@Configuration
@ComponentScan(basePackages={"cn.hutool.extra.spring"})
@EnableFeignClients
@EnableAsync(proxyTargetClass = true)
public class CoreAutoConfiguration implements WebMvcConfigurer {

    @Resource
    private CoreProperties coreProperties;

    /**
     * {@inheritDoc}
     *
     * <p>
     * 增加restApi前缀
     * </p>
     * <p>
     * ApiRestController与AdminRestController
     * </p>
     * @author miaoyj
     * @since 2020-09-21
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
        configurer
                .addPathPrefix(coreApiPathProperties.getGlobalPrefix(), c -> c.isAnnotationPresent(ApiRestController.class))
                .addPathPrefix(coreApiPathProperties.getAdminPrefix(), c -> c.isAnnotationPresent(AdminRestController.class));
    }

    /**
     * <p>springContextHolder.</p>
     *
     * @return a {@link com.boot.admin.core.util.SpringContextHolder} object.
     */
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
