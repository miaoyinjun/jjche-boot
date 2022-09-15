package org.jjche.common.system.api.fallback;

import cn.hutool.log.StaticLog;
import lombok.Setter;
import org.jjche.common.dto.*;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.vo.SecurityAppKeyBasicVO;

import java.util.List;

/**
 * <p>
 * 进入fallback的方法 检查token
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
public class SysBaseAPIFallback implements ISysBaseAPI {
    @Setter
    private Throwable cause;

    @Override
    public void logoutOnlineUser(String token) {
        StaticLog.error("token退出失败 {}", cause);
    }

    @Override
    public JwtUserDto getUserDetails() {
        StaticLog.error("认证失败 {}", cause);
        return null;
    }

    @Override
    public JwtUserDto getUserDetails(String token) {
        StaticLog.error("认证失败 token:{}, {}", token, cause);
        return null;
    }

    @Override
    public void recordLog(LogRecordDTO logRecord) {
        StaticLog.error("保存日志失败 logRecord:{}, {}", logRecord, cause);
    }

    @Override
    public void recordLogs(List<LogRecordDTO> list) {
        StaticLog.error("保存日志失败 list:{}, {}", list, cause);
    }

    @Override
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(Long userId) {
        StaticLog.error("查询用户数据权限失败 userId:{}, {}", userId, cause);
        return null;
    }

    @Override
    public SecurityAppKeyBasicVO getKeyByAppId(String appId) {
        StaticLog.error("查询应用id获取密钥失败 appId:{}, {}", appId, cause);
        return null;
    }

    @Override
    public List<DataPermissionFieldResultVO> listPermissionDataResource(PermissionDataResourceDTO dto) {
        StaticLog.error("获取数据权限失败 dto:{}, {}", dto, cause);
        return null;
    }

    @Override
    public DictParam getDictByNameValue(String name, String value) {
        StaticLog.error("获取根据字典名称 name:{}, value:{}, {}", name, value, cause);
        return null;
    }
}
