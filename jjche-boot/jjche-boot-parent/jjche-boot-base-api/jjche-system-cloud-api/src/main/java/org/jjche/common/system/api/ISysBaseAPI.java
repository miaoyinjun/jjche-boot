package org.jjche.common.system.api;

import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.ServiceNameConstant;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.system.api.factory.SysBaseAPIFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
        path = "/api/sys/base/",
        value = ServiceNameConstant.SYSTEM_SERVICE,
        fallbackFactory = SysBaseAPIFallbackFactory.class,
        configuration = MyFeignClientsConfiguration.class)
public interface ISysBaseAPI extends CommonAPI {

    @Override
    @GetMapping("logoutOnlineUser")
    void logoutOnlineUser(@RequestParam("token") String token);

    @Override
    @GetMapping("getUserDetails")
    JwtUserDto getUserDetails();

    @Override
    @PostMapping("recordLog")
    void recordLog(@RequestBody LogRecordDTO logRecord);
}
