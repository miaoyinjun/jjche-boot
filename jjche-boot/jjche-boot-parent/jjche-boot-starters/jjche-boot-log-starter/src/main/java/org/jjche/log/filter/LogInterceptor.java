package org.jjche.log.filter;

import org.jjche.common.constant.LogConstant;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * <p>
 * 日志拦截器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-10
 */
public class LogInterceptor implements HandlerInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        String xForwardedForHeader = httpServletRequest.getHeader("X-Forwarded-For");
//        String remoteIp = httpServletRequest.getRemoteAddr();
        String uuid = UUID.randomUUID().toString();
        MDC.put(LogConstant.REQUEST_ID, uuid);
        httpServletResponse.setHeader(LogConstant.REQUEST_ID, MDC.get(LogConstant.REQUEST_ID));
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        String uuid = MDC.get(REQUEST_ID);
        MDC.remove(LogConstant.REQUEST_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
