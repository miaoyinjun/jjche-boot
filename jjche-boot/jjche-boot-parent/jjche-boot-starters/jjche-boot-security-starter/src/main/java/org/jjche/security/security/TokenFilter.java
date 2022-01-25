package org.jjche.security.security;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.dto.OnlineUserDTO;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * <p>TokenFilter class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public class TokenFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);

    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final CommonAPI commonAPI;

    /**
     * <p>Constructor for TokenFilter.</p>
     *
     * @param tokenProvider Token
     * @param properties    JWT
     * @param commonAPI     api
     */
    public TokenFilter(TokenProvider tokenProvider, SecurityProperties properties,
                       CommonAPI commonAPI) {
        this.properties = properties;
        this.commonAPI = commonAPI;
        this.tokenProvider = tokenProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDTO onlineUserDto = null;
            boolean cleanUserCache = false;
            String tokenKey = null;
            try {
                SecurityJwtProperties securityJwtProperties = properties.getJwt();
                tokenKey = securityJwtProperties.getOnlineKey() + token;
                onlineUserDto = commonAPI.getOnlineUser(tokenKey);
            } catch (ExpiredJwtException e) {
                log.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    commonAPI.logoutOnlineUser(token);
                }
            }
            if (onlineUserDto != null && StringUtils.hasText(token)) {
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                // Token 续期
                tokenProvider.checkRenewal(tokenKey, onlineUserDto);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String bearerToken = request.getHeader(securityJwtProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(securityJwtProperties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(securityJwtProperties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
