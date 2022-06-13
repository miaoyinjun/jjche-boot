package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 系统基础
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@RequiredArgsConstructor
@Api(tags = "系统：基础")
@SysRestController("base")
public class SysBaseController extends BaseController {

    private final ISysBaseAPI sysBaseAPI;

    @GetMapping("/logoutOnlineUser")
    public void logoutOnlineUser(String token) {
        this.sysBaseAPI.logoutOnlineUser(token);
    }

    @AnonymousGetMapping("/getUserDetails")
    public JwtUserDto getUserDetails() {
        return this.sysBaseAPI.getUserDetails();
    }

    @PostMapping("/recordLog")
    public void recordLog(@RequestBody LogRecordDTO logRecord) {
        this.sysBaseAPI.recordLog(logRecord);
    }
}