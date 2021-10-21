package com.boot.admin.security.property;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 地址相关
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-18
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class SecurityUrlProperties {
    /**
     * 不过滤的url如 - /test/** -/demo/
     */
    private List<String> excludeUrls;
    /**
     * 角色与url对应
     */
    private List<SecurityRoleUrlProperties> roleUrls;
    /**
     * 默认不过滤的url如 - /test/** -/demo/
     */
    private List<String> excludeDefaultUrls;
}
