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
    /**
     * 支付宝
     */
    String ALI_PAY = "ali_pay";
    /**
     * 菜单
     */
    String MENU_ID = "menu:id:";
    /** Constant <code>MENU_USER_ID="menu:user:id:"</code> */
    String MENU_USER_ID = "menu:user:id:";

    /**
     * 角色信息
     */
    String ROLE_ID = "role:id:";
    /**
     * 部门信息
     */
    String DEPT_ID = "dept:id:";

    /**
     * 数据规则，用户id
     */
    String PERMISSION_DATA_RULE_USER_ID = "permission_data:rule:userid:";

    /**
     * 数据字段，用户id
     */
    String PERMISSION_DATA_FIELD_USER_ID = "permission_data:field:userid:";
}
