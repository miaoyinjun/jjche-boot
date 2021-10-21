package com.boot.admin.common.constant;

/**
 * <p>关于缓存的Key集合.</p>
 *
 * @author: liaojinlong
 * @since: 2020/6/11 15:49
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 */
public interface CacheKey {

    /**
     * 用户
     */
    String USER_ID = "user:id:";
    /** Constant <code>USER_NAME="user:username:"</code> */
    String USER_NAME = "user:username:";
    /** Constant <code>JWT_USER_NAME="user:jwt:username:"</code> */
    String JWT_USER_NAME = "user:jwt:username:";

    /**
     * 字典
     */
    String DIC_NAME = "dic:name:";
    /**
     * 七牛
     */
    String QI_NIU = "qiniu";
    /**
     * 邮箱
     */
    String EMAIL = "email";
    /** Constant <code>EMAIL_VERIFY_CODE="email_verify_code"</code> */
    String EMAIL_VERIFY_CODE = "email_verify_code";
    /**
     * 支付宝
     */
    String ALI_PAY = "ali_pay";
    /**
     * 数据
     */
    String DATE_USER_ID = "data:user:id:";
    /**
     * 菜单
     */
    String MENU_ID = "menu:id:";
    /** Constant <code>MENU_USER_ID="menu:user:id:"</code> */
    String MENU_USER_ID = "menu:user:id:";
    /**
     * 角色授权
     */
    String ROLE_AUTH = "role:auth:";
    /**
     * 角色授权
     */
    String ROLE_AUTH_ROLE = "role:role:";

    /**
     * 角色信息
     */
    String ROLE_ID = "role:id:";
    /**
     * 部门信息
     */
    String DEPT_ID = "dept:id:";
}
