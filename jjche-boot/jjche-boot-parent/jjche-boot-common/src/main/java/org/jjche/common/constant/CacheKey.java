package org.jjche.common.constant;

/**
 * <p>关于缓存的Key集合.</p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @author: liaojinlong
 * @since: 2020/6/11 15:49
 */
public interface CacheKey {

    /**
     * 限流脚本
     */
    String SCRIPT_LUA_LIMIT = "local c" +
            "\nc = redis.call('get',KEYS[1])" +
            "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
            "\nreturn c;" +
            "\nend" +
            "\nc = redis.call('incr',KEYS[1])" +
            "\nif tonumber(c) == 1 then" +
            "\nredis.call('expire',KEYS[1],ARGV[2])" +
            "\nend" +
            "\nreturn c;";

    /**
     * Constant <code>JWT_USER_NAME="user:jwt:username:"</code>
     */
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
    /**
     * Constant <code>MENU_USER_ID="menu:user:id:"</code>
     */
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

    /**
     * gateway路由缓存
     */
    String GATEWAY_ROUTES = "sys:cache:cloud:gateway_routes";

    /**
     * 密钥
     */
    String SECURITY_APP_ID = "sys:security:app:id:";

    /**
     * 限速
     */
    String REQUEST_LIMIT = "sys:request:limit:";
}
