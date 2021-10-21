package com.boot.admin.filter.property;

import com.boot.admin.common.constant.FilterEncryptionConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 加密属性配置加载类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Data
@Component
@ConfigurationProperties(FilterEncryptionConstant.PROPERTY_PACKAGE_PREFIX)
public class FilterEncryptionProperties {
    /**
     * 过滤的地址如/test
     */
    private List<String> filterUrls;
    /**
     * 排除的地址如- /test/** - /demo/
     */
    private List<String> excludeUrls;
    /**
     * 加密类型：MD5、RSA
     */
    private String type = FilterEncryptionConstant.TYPE_MD5;
    /**
     * 是否激活
     */
    private boolean enabled = true;
    /**
     * 超时时效，毫秒
     */
    private long timestamp = FilterEncryptionConstant.EXPIRE_TIME;

    /**
     * 随机数长度
     */
    private int nonceLength = FilterEncryptionConstant.NONCE_LENGTH;

    /**
     * 客户端信息
     */
    private List<FilterEncryptionApplicationProperties> applications;
}
