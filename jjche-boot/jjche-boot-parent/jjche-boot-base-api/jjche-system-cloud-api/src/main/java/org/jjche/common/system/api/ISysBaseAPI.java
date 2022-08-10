package org.jjche.common.system.api;

import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.constant.ServiceNameConstant;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.system.api.factory.SysBaseAPIFallbackFactory;
import org.jjche.common.vo.SecurityAppKeyBasicVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * cloud接口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Component
@FeignClient(contextId = "sysBaseRemoteApi",
        path = "/sys/base/",
        value = ServiceNameConstant.SYSTEM_SERVICE,
        fallbackFactory = SysBaseAPIFallbackFactory.class)
public interface ISysBaseAPI extends CommonAPI {

    @Override
    @GetMapping(URL_LOGOUT_ONLINE_USER)
    void logoutOnlineUser(@RequestParam("token") String token);

    @Override
    @GetMapping(URL_GET_USER_DETAILS)
    JwtUserDto getUserDetails();

    @Override
    @GetMapping(URL_GET_USER_DETAILS)
    JwtUserDto getUserDetails(@RequestHeader(SecurityConstant.HEADER_AUTH) String token);

    @Override
    @PostMapping(URL_RECORD_LOG)
    void recordLog(@RequestBody LogRecordDTO logRecord);

    @Override
    @PostMapping(URL_RECORD_LOGS)
    void recordLogs(@RequestBody List<LogRecordDTO> list);

    @Override
    @GetMapping(URL_LIST_PERMISSION_DATA_RULE_BY_USER_ID)
    List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam("userId") Long userId);

    @Override
    @GetMapping(URL_GET_KEY_BY_APP_ID)
    SecurityAppKeyBasicVO getKeyByAppId(@RequestParam("appId") String appId);
}
