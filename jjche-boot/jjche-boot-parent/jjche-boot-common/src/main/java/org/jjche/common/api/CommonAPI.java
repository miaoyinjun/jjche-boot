package org.jjche.common.api;

import org.springframework.security.core.Authentication;

public interface CommonAPI {

    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    void logoutOnlineUser(String token);

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @return /
     */
    Authentication getCheckAuthentication();
}