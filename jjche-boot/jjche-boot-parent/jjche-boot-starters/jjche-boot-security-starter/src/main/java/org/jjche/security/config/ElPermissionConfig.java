package org.jjche.security.config;

import cn.hutool.core.collection.CollUtil;
import org.jjche.common.context.ElPermissionContext;
import org.jjche.core.util.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>ElPermissionConfig class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Service(value = "el")
public class ElPermissionConfig {

    /**
     * <p>check.</p>
     *
     * @param permissions a {@link java.lang.String} object.
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean check(String... permissions) {
        List<String> permissionList = CollUtil.newArrayList(permissions);
        if (CollUtil.isNotEmpty(permissionList)) {
            ElPermissionContext.set(CollUtil.getFirst(permissionList));
        }
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return SecurityUtils.isAdmin() || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
