package org.jjche.common.api;

import org.jjche.common.dto.*;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;

import java.util.List;

public interface CommonAPI {

    /**
     * 清除在线用户token {@value}
     */
    String URL_LOGOUT_ONLINE_USER = "logoutOnlineUser";
    /**
     * 获取认证 {@value}
     */
    String URL_GET_USER_DETAILS = "getUserDetails";
    /**
     * 根据参数token获取认证信息 {@value}
     */
    String URL_RECORD_LOG = "recordLog";
    /**
     * 记录日志 {@value}
     */
    String URL_RECORD_LOGS = "recordLogs";
    /**
     * 批量记录日志 {@value}
     */
    String URL_LIST_PERMISSION_DATA_RULE_BY_USER_ID = "listPermissionDataRuleByUserId";
    /**
     * 根据应用id获取密钥 {@value}
     */
    String URL_GET_KEY_BY_APP_ID = "getKeyByAppId";
    /**
     * 获取数据权限 {@value}
     */
    String URL_LIST_PERMISSION_DATA_RESOURCE = "listPermissionDataResource";
    /**
     * 获取根据字典名称和值 {@value}
     */
    String URL_GET_DICT_BY_NAME_VALUE = "getDictByNameValue";

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

    /**
     * <p>
     * 根据应用id获取密钥
     * </p>
     *
     * @param appId 应用id
     * @return /
     */
    SecurityAppKeyBasicVO getKeyByAppId(String appId);

    /**
     * <p>
     * 执行过滤
     * </p>
     *
     * @param dto /
     * @return 结果
     */
    List<DataPermissionFieldResultVO> listPermissionDataResource(PermissionDataResourceDTO dto);

    /**
     * <p>
     * 获取根据字典名称和值
     * </p>
     *
     * @param name  字典名称
     * @param value 字典值
     * @return /
     */
    DictParam getDictByNameValue(String name, String value);
}