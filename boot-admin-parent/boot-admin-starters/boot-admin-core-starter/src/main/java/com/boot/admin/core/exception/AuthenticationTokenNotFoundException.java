package com.boot.admin.core.exception;

import lombok.Getter;

/**
 * <p>
 * 获取授权信息错误
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-24
 * @version 1.0.8-SNAPSHOT
 */
@Getter
public class AuthenticationTokenNotFoundException extends RuntimeException {
    /**
     * <p>Constructor for AuthenticationTokenNotFoundException.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public AuthenticationTokenNotFoundException(String msg) {
        super(msg);
    }
}
