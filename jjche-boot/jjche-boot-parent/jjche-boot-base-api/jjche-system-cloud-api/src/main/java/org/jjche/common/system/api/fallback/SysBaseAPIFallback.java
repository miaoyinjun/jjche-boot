package org.jjche.common.system.api.fallback;

import cn.hutool.log.StaticLog;
import lombok.Setter;
import org.jjche.common.system.api.ISysBaseAPI;
import org.springframework.security.core.Authentication;

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
    public Authentication getCheckAuthentication() {
        StaticLog.error("认证失败 {}", cause);
        return null;
    }
}
