package org.jjche.demo.modules.provider.feign.fallback;

import org.jjche.demo.modules.provider.feign.ProviderApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 测试Fallback
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-21
 */
@Component
public class ProviderApiFallback implements FallbackFactory<ProviderApi> {

    @Override
    public ProviderApi create(Throwable throwable) {
        return null;
    }
}