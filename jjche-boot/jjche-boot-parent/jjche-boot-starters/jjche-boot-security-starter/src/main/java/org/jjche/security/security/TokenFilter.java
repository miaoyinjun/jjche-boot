package org.jjche.security.security;

import org.jjche.common.api.CommonAPI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>TokenFilter class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
public class TokenFilter extends GenericFilterBean {
    private final CommonAPI commonAPI;

    /**
     * <p>Constructor for TokenFilter.</p>
     *
     * @param commonAPI api
     */
    public TokenFilter(CommonAPI commonAPI) {
        this.commonAPI = commonAPI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        //认证token
        Authentication authentication = commonAPI.getCheckAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
