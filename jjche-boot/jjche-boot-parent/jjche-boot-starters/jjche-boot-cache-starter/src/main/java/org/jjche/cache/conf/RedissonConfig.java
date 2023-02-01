package org.jjche.cache.conf;

import org.jjche.common.util.StrUtil;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    /**
     * <p>
     * 兼容redisson有/无密码报错问题
     * 密码为空时将密码设置为null，这样就会走无密码认证
     * </p>
     *
     * @return /
     */
    @Bean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer() {
        return configuration -> {
            if (StrUtil.isEmpty(configuration.useSingleServer().getPassword())) {
                configuration.useSingleServer().setPassword(null);
            }
        };
    }
}
