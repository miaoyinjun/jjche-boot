package org.jjche.filter;

import cn.hutool.core.util.IdUtil;
import org.jjche.common.constant.LogConstant;
import org.jjche.common.context.ContextUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 全局上下文拦截器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-05-10
 */
public class ContextInterceptor implements HandlerInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //日志请求id
        String uuid = IdUtil.randomUUID();
        MDC.put(LogConstant.REQUEST_ID, uuid);
        httpServletResponse.setHeader(LogConstant.REQUEST_ID, uuid);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清理
        MDC.remove(LogConstant.REQUEST_ID);
        ContextUtil.remove();
    }
}
