package com.boot.admin.core.property;

import com.boot.admin.core.constant.CoreConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 告警配置
 * </p>
 *
 * @author miaoyj
 * @since 2020-12-17
 * @version 1.0.10-SNAPSHOT
 */
@Data
@Component
@ConfigurationProperties(prefix = CoreConstant.PROPERTY_CORE_PACKAGE_PREFIX)
public class CoreProperties {
    /**
     * api
     */
    private CoreApiProperties api;
    /**
     * 告警
     */
    private CoreAlarmProperties alarm;
}
