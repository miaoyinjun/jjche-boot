package org.jjche.demo.modules.student.feign.fallback;

import org.jjche.demo.modules.student.feign.JeecgHelloApi;
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
public class JeecgHelloFallback implements FallbackFactory<JeecgHelloApi> {

    @Override
    public JeecgHelloApi create(Throwable throwable) {
        return null;
    }
}