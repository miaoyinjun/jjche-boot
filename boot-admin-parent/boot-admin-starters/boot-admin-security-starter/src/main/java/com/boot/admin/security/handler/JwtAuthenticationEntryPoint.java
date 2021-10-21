package com.boot.admin.security.handler;

import com.boot.admin.common.util.HttpUtil;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 认证异常
 * </p>
 * <p>
 * 解决不提供token时，返回403问题
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-25
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /** {@inheritDoc} */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        HttpUtil.printJson(response, ResultWrapper.tokenError());
    }
}
