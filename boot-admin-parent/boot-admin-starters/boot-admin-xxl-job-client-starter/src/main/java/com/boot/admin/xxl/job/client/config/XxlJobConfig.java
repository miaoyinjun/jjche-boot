package com.boot.admin.xxl.job.client.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 配置类
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-19
 * @version 1.0.0-SNAPSHOT
 */
@Data
@ConfigurationProperties
public class XxlJobConfig {
    @Value("${boot.admin.xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${boot.admin.xxl.job.accessToken}")
    private String accessToken;

    @Value("${boot.admin.xxl.job.executor.appName}")
    private String appName;

    @Value("${boot.admin.xxl.job.executor.address}")
    private String address;

    @Value("${boot.admin.xxl.job.executor.ip}")
    private String ip;

    @Value("${boot.admin.xxl.job.executor.port}")
    private int port;

    @Value("${boot.admin.xxl.job.executor.logpath}")
    private String logPath;

    @Value("${boot.admin.xxl.job.executor.logretentiondays}")
    private int logRetentionDays;
}
