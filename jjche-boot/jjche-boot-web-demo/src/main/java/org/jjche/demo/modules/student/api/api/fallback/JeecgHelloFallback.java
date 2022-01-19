package org.jjche.demo.modules.student.api.api.fallback;

import org.jjche.demo.modules.student.api.api.JeecgHelloApi;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author zyf
 */
@Component
public class JeecgHelloFallback implements FallbackFactory<JeecgHelloApi> {

    @Override
    public JeecgHelloApi create(Throwable throwable) {
        return null;
    }
}