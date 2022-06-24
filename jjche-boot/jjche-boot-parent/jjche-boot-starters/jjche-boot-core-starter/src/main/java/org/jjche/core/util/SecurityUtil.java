package org.jjche.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.constant.UserConstant;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.util.StrUtil;
import org.jjche.common.wrapper.enums.ResultWrapperCodeEnum;
import org.jjche.core.exception.AuthenticationTokenExpiredException;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获取当前登录的用户
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-17
 */
public class SecurityUtil {
    /**
     * 获取系统用户名称
     *
     * @return 系统用户名称
     */
    public static String getUsername() {
        return getHeaderByAuthException(SecurityConstant.JWT_KEY_USERNAME);
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
    public static String getUsernameOrDefaultUsername() {
        try {
            // 这里应根据实际业务情况获取具体信息
            return getUsername();
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
    public static Long getUserId() {
        return getLongHeaderByAuthException(SecurityConstant.JWT_KEY_USER_ID);
    }

    /**
     * 获取当前用户的数据权限
     *
     * @return /
     */
    public static DataScope getUserDataScope() {
        DataScope dataScope = new DataScope();
        Set<String> deptStrIds = RequestHolder.getHeaders(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS);
        Set<Long> deptIds = new HashSet<>();
        if (CollUtil.isNotEmpty(deptStrIds)) {
            deptIds = deptStrIds.stream().map(o -> Long.parseLong(o)).collect(Collectors.toSet());
        }
        String isAllStr = getHeaderByAuthException(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL);
        Boolean isAll = false;
        if (StrUtil.isNotBlank(isAllStr)) {
            isAll = Boolean.parseBoolean(isAllStr);
        }
        String isSelfStr = getHeaderByAuthException(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF);
        Boolean isSelf = false;
        if (StrUtil.isNotBlank(isSelfStr)) {
            isSelf = Boolean.parseBoolean(isSelfStr);
        }
        String userIdStr = getHeaderByAuthException(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID);
        Long userid = 0L;
        if (StrUtil.isNotBlank(userIdStr)) {
            userid = Long.parseLong(userIdStr);
        }
        String username = RequestHolder.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME);

        dataScope.setDeptIds(deptIds);
        dataScope.setAll(isAll);
        dataScope.setAll(isSelf);
        dataScope.setUserId(userid);
        dataScope.setUserName(username);
        return dataScope;
    }

    /**
     * <p>
     * 是否admin
     * </p>
     *
     * @return 是否admin
     */
    public static Boolean isAdmin() {
        Set<String> permissions = listPermission();
        return permissions.contains(SecurityConstant.ADMIN_PERMISSION);
    }

    /**
     * <p>
     * 获取权限列表
     * </p>
     *
     * @return /
     */
    public static Set<String> listPermission() {
        return RequestHolder.getHeaders(SecurityConstant.JWT_KEY_PERMISSION);
    }

    /**
     * <p>
     * 获取token
     * </p>
     *
     * @author miaoyj
     * @since 2022-06-22
     */
    public static String getToken() {
        String bearerToken = getHeaderByAuthException(SecurityConstant.HEADER_AUTH);
        String tokenStartWith = SecurityConstant.TOKEN_START_WITH;
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenStartWith)) {
            // 去掉令牌前缀
            return bearerToken.replace(tokenStartWith, "");
        } else {
            StaticLog.warn("非法Token：{}", bearerToken);
        }
        return null;
    }

    /**
     * <p>
     * 获取header，验证用户信息抛出异常
     * </p>
     *
     * @author miaoyj
     * @since 2022-06-23
     */
    private static String getHeaderByAuthException(String headerName) {
        String value = RequestHolder.getHeader(headerName);
        if (StrUtil.isNotBlank(value)) {
            return value;
        }
        throw new AuthenticationTokenExpiredException(ResultWrapperCodeEnum.TOKEN_EXPIRED.getMsg());
    }

    /**
     * <p>
     * 获取header，验证用户信息抛出异常
     * </p>
     *
     * @author miaoyj
     * @since 2022-06-23
     */
    private static Long getLongHeaderByAuthException(String headerName) {
        Long value = RequestHolder.getHeaderLong(headerName);
        if (value != null && value > 0) {
            return value;
        }
        throw new AuthenticationTokenExpiredException(ResultWrapperCodeEnum.TOKEN_EXPIRED.getMsg());
    }
}
