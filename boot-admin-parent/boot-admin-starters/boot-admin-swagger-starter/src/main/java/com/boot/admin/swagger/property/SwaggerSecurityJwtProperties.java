package com.boot.admin.swagger.property;

import lombok.Data;

/**
 * <p>
 * jwt属性配置类
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-25
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class SwaggerSecurityJwtProperties {
    /**
     * 是否开启
     */
    private boolean enabled;
    /**
     * 认证header
     */
    private String tokenHeader;

    /**
     * 认证前缀
     */
    private String tokenStartWith;
}
