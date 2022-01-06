package org.jjche.xxl.job.client.config;

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
    @Value("${jjche.xxl.job.admin.addresses}")
    private String adminAddresses;

    @Value("${jjche.xxl.job.accessToken}")
    private String accessToken;

    @Value("${jjche.xxl.job.executor.appName}")
    private String appName;

    @Value("${jjche.xxl.job.executor.address}")
    private String address;

    @Value("${jjche.xxl.job.executor.ip}")
    private String ip;

    @Value("${jjche.xxl.job.executor.port}")
    private int port;

    @Value("${jjche.xxl.job.executor.logpath}")
    private String logPath;

    @Value("${jjche.xxl.job.executor.logretentiondays}")
    private int logRetentionDays;
}
