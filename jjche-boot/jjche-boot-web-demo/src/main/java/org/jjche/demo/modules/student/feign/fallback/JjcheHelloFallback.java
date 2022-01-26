package org.jjche.demo.modules.student.feign.fallback;

import org.jjche.demo.modules.student.feign.JjcheHelloApi;
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
public class JjcheHelloFallback implements FallbackFactory<JjcheHelloApi> {

    @Override
    public JjcheHelloApi create(Throwable throwable) {
        return null;
    }
}