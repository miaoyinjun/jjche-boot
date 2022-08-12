package org.jjche.security.security;

import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.wrapper.HeaderMapRequestWrapper;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

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
        String token = reqWrapper.getHeader(SecurityConstant.HEADER_AUTH);
        if (userDetails != null) {
            Authentication authentication = null;
            UserVO userVO = userDetails.getUser();
            UserTypeEnum userType = userVO.getUserType();
            //设置用户信息上下文
            setContextUser(userDetails, token);
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

    /**
     * <p>
     * 设置用户信息上下文
     * </p>
     *
     * @param userDetails 用户
     * @param token       /
     */
    private void setContextUser(JwtUserDto userDetails, String token) {
        if (userDetails != null) {
            ContextUtil.setToken(token);
            UserVO user = userDetails.getUser();
            //用户基本信息
            Long userId = user.getId();
            String username = user.getUsername();
            Set<String> elPermissions = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
            //数据范围
            DataScope dataScope = userDetails.getDataScope();
            //部门id
            Set<Long> dataScopeDeptIds = dataScope.getDeptIds();
            //全部
            Boolean dataScopeIsAll = dataScope.isAll();
            //本人
            Boolean dataScopeIsSelf = dataScope.isSelf();
            //用户id
            Long dataScopeUserid = dataScope.getUserId();
            //用户名
            String dataScopeUsername = dataScope.getUserName();

            ContextUtil.setUserId(userId);
            ContextUtil.setUsername(username);
            ContextUtil.setPermissions(elPermissions);
            //数据范围
            ContextUtil.setDataScopeDeptIds(dataScopeDeptIds);
            ContextUtil.setDataScopeIsAll(dataScopeIsAll);
            ContextUtil.setDataScopeIsSelf(dataScopeIsSelf);
            ContextUtil.setDataScopeUserId(dataScopeUserid);
            ContextUtil.setDataScopeUserName(dataScopeUsername);
        }
    }

}
