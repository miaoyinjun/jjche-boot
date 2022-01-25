package org.jjche.common.api;

import org.jjche.common.dto.OnlineUserDTO;

public interface CommonAPI {
    /**
     * <p>
     * 查询在线用户
     * </p>
     *
     * @param tokenKey /
     * @return /
     */
    OnlineUserDTO getOnlineUser(String tokenKey);

    /**
     * <p>
     * 清除在线用户token
     * </p>
     *
     * @param token /
     */
    void logoutOnlineUser(String token);
}