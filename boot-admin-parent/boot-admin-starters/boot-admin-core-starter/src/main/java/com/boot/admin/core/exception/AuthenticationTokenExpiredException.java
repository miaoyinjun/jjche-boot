package com.boot.admin.core.exception;

import lombok.Getter;

/**
 * <p>
 * 获取授权信息错误，过期
 * </p>
 *
 * @author miaoyj
 * @since 2021-09-01
 * @version 1.0.0-SNAPSHOT
 */
@Getter
public class AuthenticationTokenExpiredException extends RuntimeException {
    /**
     * <p>Constructor for AuthenticationTokenNotFoundException.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public AuthenticationTokenExpiredException(String msg) {
        super(msg);
    }
}
