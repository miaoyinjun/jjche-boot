package org.jjche.common.constant;

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
    String PROPERTY_SECURITY_PACKAGE_PREFIX = "jjche.security";

    /**
     * 匿名访问的地址
     */
    Set<String> ANONYMOUS_URLS = new HashSet<>();

    /**
     * 超级管理员权限标识
     */
    String ADMIN_PERMISSION = "admin";

    /**
     * token 标识
     * 修改配置文件时同步要修改这个
     */
    String HEADER_AUTH = "Authorization";

    /**
     * token 开头标识
     * 修改配置文件时同步要修改这个
     */
    String TOKEN_START_WITH = "Bearer";

    /**
     * 用户id
     */
    String JWT_KEY_USER_ID = "userid";

    /**
     * 用户名称
     */
    String JWT_KEY_USERNAME = "username";

    /**
     * 用户权限
     */
    String JWT_KEY_PERMISSION = "permissions";
    /**
     * 用户数据范围-部门ids
     */
    String JWT_KEY_DATA_SCOPE_DEPT_IDS = "data_scope_dept_ids";

    /**
     * 用户数据范围-全部
     */
    String JWT_KEY_DATA_SCOPE_IS_ALL = "data_scope_is_all";

    /**
     * 用户数据范围-本人
     */
    String JWT_KEY_DATA_SCOPE_IS_SELF = "data_scope_is_self";

    /**
     * 用户数据范围-用户id
     */
    String JWT_KEY_DATA_SCOPE_USERID = "data_scope_userid";

    /**
     * 用户数据范围-用户名
     */
    String JWT_KEY_DATA_SCOPE_USERNAME = "data_scope_username";

    /**
     * 密码验证服务名
     */
    String USER_DETAILS_PWD_SERVICE = "userDetailsService";

    /**
     * 短信验证服务名
     */
    String USER_DETAILS_SMS_SERVICE = "smsUserDetailsService";
}
