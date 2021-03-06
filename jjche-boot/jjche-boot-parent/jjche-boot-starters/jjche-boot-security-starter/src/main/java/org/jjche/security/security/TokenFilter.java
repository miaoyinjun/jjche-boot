package org.jjche.security.security;

import org.jjche.common.api.CommonAPI;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.wrapper.HeaderMapRequestWrapper;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HeaderMapRequestWrapper reqWrapper = new HeaderMapRequestWrapper((HttpServletRequest) servletRequest);
        //非cloud才会走这里
        JwtUserDto userDetails = commonAPI.getUserDetails();
        if (userDetails != null) {
            Authentication authentication = null;
            UserVO userVO = userDetails.getUser();
            UserTypeEnum userType = userVO.getUserType();
            //用户信息到header
            Map<String, Object> userHeaders = HttpUtil.getUserHeaders(userDetails);
            reqWrapper.addHeaders(userHeaders);
            //密码
            if (UserTypeEnum.PWD == userType) {
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            }//短信
            else if (UserTypeEnum.SMS == userType) {
                authentication = new SmsCodeAuthenticationToken(userVO.getUsername());
            }
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(reqWrapper, servletResponse);
    }

}
