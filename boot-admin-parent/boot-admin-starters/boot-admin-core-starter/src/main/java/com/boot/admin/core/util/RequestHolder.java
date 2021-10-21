package com.boot.admin.core.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取 HttpServletRequest
 *
 * @author Zheng Jie
 * @since 2018-11-24
 * @version 1.0.8-SNAPSHOT
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
}
