package com.boot.admin.security;

import com.boot.admin.security.property.SecurityProperties;
import com.boot.admin.security.security.TokenProvider;
import com.boot.admin.security.service.JwtUserServiceImpl;
import com.boot.admin.security.service.OnlineUserService;
import com.boot.admin.security.config.ElPermissionConfig;
import com.boot.admin.security.config.SecurityConfig;
import com.boot.admin.security.handler.JwtAuthenticationAccessDeniedHandler;
import com.boot.admin.security.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 权限配置入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Configuration
@Import({
        SecurityProperties.class,
        JwtAuthenticationAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class, SecurityConfig.class,
        OnlineUserService.class, TokenProvider.class, ElPermissionConfig.class,
        JwtUserServiceImpl.class
})
public class SecurityAutoConfiguration {
}
