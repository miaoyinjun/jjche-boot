package org.jjche.filter;

import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.jjche.common.constant.LogConstant;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.context.ContextUtil;
import org.jjche.core.util.RequestHolder;
import org.jjche.core.util.SpringContextHolder;
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
        //这里使用skyWalking的traceId替代了MDC.uuid
        //日志请求id
//        String uuid = IdUtil.randomUUID();
//        MDC.put(LogConstant.REQUEST_ID, uuid);
        httpServletResponse.setHeader(LogConstant.REQUEST_ID, TraceContext.traceId());
        if (SpringContextHolder.isCloud()) {
            //用户基本信息
            ContextUtil.setUserId(RequestHolder.getHeaderLong(SecurityConstant.JWT_KEY_USER_ID));
            ContextUtil.setUsername(RequestHolder.getHeader(SecurityConstant.JWT_KEY_USERNAME));
            ContextUtil.setPermissions(RequestHolder.getHeaders(SecurityConstant.JWT_KEY_PERMISSION));

            //数据范围
            ContextUtil.setDataScopeDeptIds(RequestHolder.getLongHeaders(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS));
            ContextUtil.setDataScopeIsAll(RequestHolder.getBooleanHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL));
            ContextUtil.setDataScopeIsSelf(RequestHolder.getBooleanHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF));
            ContextUtil.setDataScopeUserId(RequestHolder.getLongHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID));
            ContextUtil.setDataScopeUserName(RequestHolder.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME));
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //清理
//        MDC.remove(LogConstant.REQUEST_ID);
        ContextUtil.remove();
    }
}
