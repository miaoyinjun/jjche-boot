package org.jjche.security.property;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * security认证角色与地址配置配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Data
public class SecurityRoleUrlProperties {
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 地址
     */
    private List<String> urls;
}
