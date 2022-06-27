package org.jjche.common.system.api.factory;

import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.system.api.fallback.SysBaseAPIFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 失败
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Component
public class SysBaseAPIFallbackFactory implements FallbackFactory<ISysBaseAPI> {

    @Override
    public ISysBaseAPI create(Throwable throwable) {
        SysBaseAPIFallback fallback = new SysBaseAPIFallback();
        fallback.setCause(throwable);
        return fallback;
    }
}