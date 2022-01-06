package org.jjche.security.security;

import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.service.OnlineUserService;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>TokenConfigurer class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final OnlineUserService onlineUserService;
    private final RedisService redisService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, properties, onlineUserService, redisService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
