package com.boot.admin.property;

import lombok.Data;

/**
 * <p>
 * 阿里云短信配置
 * </p>
 *
 * @author miaoyj
 * @since 2021-03-23
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class AliYunSmsCodeProperties {
    private String accessKeyId;
    private String accessSecret;
    private String signName;
    private Long timeInterval = 10 * 60 * 1000L;
    private String templateCodeRegister;
    private String templateCodeForgetPwd;
    private String templateCodeLogin;
}
