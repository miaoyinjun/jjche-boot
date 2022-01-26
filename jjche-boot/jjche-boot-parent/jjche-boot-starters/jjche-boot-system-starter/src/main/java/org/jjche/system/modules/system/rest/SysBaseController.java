package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

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

    @AnonymousGetMapping("/getCheckAuthentication")
    public Authentication getCheckAuthentication() {
        return this.sysBaseAPI.getCheckAuthentication();
    }
}