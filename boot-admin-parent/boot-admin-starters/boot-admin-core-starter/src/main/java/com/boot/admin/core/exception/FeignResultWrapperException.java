package com.boot.admin.core.exception;

/**
 * <p>
 * Feign调用ResultWrapper异常
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-13
 * @version 1.0.0-SNAPSHOT
 */
public class FeignResultWrapperException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for FeignResultWrapperException.</p>
     *
     * @param msg a {@link java.lang.String} object.
     */
    public FeignResultWrapperException(String msg) {
        super(msg);
    }
}
