package org.jjche.common.api;

import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;

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
    JwtUserDto getUserDetails();

    /**
     * <p>
     * 根据参数token获取认证信息
     * </p>
     *
     * @param token /
     * @return /
     */
    JwtUserDto getUserDetails(String token);

    void recordLog(LogRecordDTO logRecord);
}