package com.boot.admin.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 属性加载
 * </p>
 *
 * @author miaoyj
 * @since 2021-01-05
 * @version 1.0.10-SNAPSHOT
 */
@Data
@Component
@ConfigurationProperties(prefix = "boot.admin")
public class AdminProperties {

    /**
     * 任务
     */
    private AsyncTaskProperties task;

    /**
     * 用户
     */
    private UserProperties user;

    /**
     * 短信
     */
    private AliYunSmsCodeProperties sms;
}
