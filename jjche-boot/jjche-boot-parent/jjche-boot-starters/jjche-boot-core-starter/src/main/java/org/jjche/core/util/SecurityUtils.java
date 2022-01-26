package org.jjche.core.util;

import cn.hutool.json.JSONObject;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.constant.UserConstant;
import org.jjche.common.pojo.DataScope;
import org.jjche.core.exception.AuthenticationTokenExpiredException;
import org.jjche.core.exception.AuthenticationTokenNotFoundException;
import org.jjche.core.wrapper.enums.ResultWrapperCodeEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 获取当前登录的用户
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-17
 */
public class SecurityUtils {

    /**
     * 获取当前登录的用户
     *
     * @return UserDetails
     */
    public static UserDetails getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationTokenExpiredException(ResultWrapperCodeEnum.TOKEN_EXPIRED.getMsg());
        }
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails;
        }
        throw new AuthenticationTokenNotFoundException(ResultWrapperCodeEnum.TOKEN_NOT_FOUND.getMsg());
    }

    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * <p>
     * 获取系统用户或默认默认名称
     * </p>
     * <p>
     * 用户定时任务，或者无Token调用的情况
     * </p>
     *
     * @return 用户名
     * @author miaoyj
     * @since 2020-12-17
     */
    public static String getCurrentOrDefaultUsername() {
        try {
            // 这里应根据实际业务情况获取具体信息
            return getCurrentUsername();
        } catch (Exception ignored) {
        }
        // 用户定时任务，或者无Token调用的情况
        return UserConstant.DEFAULT_SYSTEM_USER_NAME;
    }

    /**
     * 获取系统用户ID
     *
     * @return 系统用户ID
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(new JSONObject(userDetails).get("user")).get("id", Long.class);
    }

    /**
     * 获取当前用户的数据权限
     *
     * @return /
     */
    public static DataScope getCurrentUserDataScope() {
        UserDetails userDetails = getCurrentUser();
        return new JSONObject(userDetails).get("dataScope", DataScope.class);
    }

    /**
     * <p>
     * 是否admin
     * </p>
     *
     * @return 是否admin
     */
    public static Boolean isAdmin() {
        List<String> elPermissions = getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return elPermissions.contains(SecurityConstant.ADMIN_PERMISSION);
    }
}
