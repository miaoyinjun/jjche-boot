package org.jjche.core.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;

/**
 * 获取 HttpServletRequest
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
public class RequestHolder {

    /**
     * <p>getHttpServletRequest.</p>
     *
     * @return a {@link javax.servlet.http.HttpServletRequest} object.
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static String getHeader(String headerName) {
        return getHttpServletRequest().getHeader(headerName);
    }

    /**
     * <p>
     * 获取头部信息
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Long getHeaderLong(String headerName) {
        return Convert.toLong(getHeader(headerName));
    }

    /**
     * <p>
     * 获取头部集合
     * </p>
     *
     * @param headerName 头部名称
     * @return /
     */
    public static Set<String> getHeaders(String headerName) {
        Enumeration<String> values = getHttpServletRequest().getHeaders(headerName);
        return CollUtil.newHashSet(true, values);
    }
}
