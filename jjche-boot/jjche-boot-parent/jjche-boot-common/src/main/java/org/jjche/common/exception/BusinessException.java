package org.jjche.common.exception;

/**
 * <p>
 * 业务自定义异常
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-13
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for SignException.</p>
     */
    public BusinessException(String msg) {
        super(msg);
    }
}
