package com.boot.admin.security.property;

import com.boot.admin.common.constant.SecurityConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt参数配置
 *
 * @author Zheng Jie
 * @since 2019年11月28日
 * @version 1.0.8-SNAPSHOT
 */
@Data
@ConfigurationProperties(prefix = SecurityConstant.PROPERTY_SECURITY_PACKAGE_PREFIX)
public class SecurityProperties {
    /**
     * 令牌
     */
    private SecurityJwtProperties jwt;
    /**
     * 登录
     */
    private SecurityLoginProperties login;
    /**
     * 地址
     */
    private SecurityUrlProperties url;
    /**
     * rsa
     */
    private SecurityRsaProperties rsa;

}
