package com.boot.admin.common.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 安全定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-12
 */
public interface SecurityConstant {
    /**
     * 属性路径前缀{@value}
     */
    String PROPERTY_SECURITY_PACKAGE_PREFIX = "boot.admin.security";

    /**
     * 匿名访问的地址
     */
    Set<String> ANONYMOUS_URLS = new HashSet<>();

    /** Constant <code>ADMIN_PERMISSION="admin"</code> */
    String ADMIN_PERMISSION = "admin";
}
