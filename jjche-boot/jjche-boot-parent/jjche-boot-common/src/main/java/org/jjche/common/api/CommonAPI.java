package org.jjche.common.api;

import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;

import java.util.List;

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

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param logRecord /
     */
    void recordLog(LogRecordDTO logRecord);

    /**
     * <p>
     * 批量记录日志
     * </p>
     *
     * @param list /
     */
    void recordLogs(List<LogRecordDTO> list);

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId);
}