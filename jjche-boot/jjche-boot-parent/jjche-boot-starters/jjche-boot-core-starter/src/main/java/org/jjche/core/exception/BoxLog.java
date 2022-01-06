package org.jjche.core.exception;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 日志记录
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-17
 */
@Data
public class BoxLog implements Serializable {
    /**
     * browser name
     */
    String browser;
    /**
     * request uri
     */
    private String requestUri;
    /**
     * request method
     */
    private String requestMethod;
    /**
     * request ip
     */
    private String requestIp;
    /**
     * request headers
     */
    private Map<String, String> requestHeaders;
    /**
     * request param
     */
    private Map<String, String> requestParams;
    /**
     * request body
     */
    private String requestBody;
    /**
     * exception stack
     */
    private String exceptionStack;
}
