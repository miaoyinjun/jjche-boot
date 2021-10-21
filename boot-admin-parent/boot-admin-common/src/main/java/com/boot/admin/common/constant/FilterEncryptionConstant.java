package com.boot.admin.common.constant;

/**
 * <p>
 * 加密定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-12
 */
public interface FilterEncryptionConstant {
    /**
     * 类型 {@value}
     */
    String TYPE_MD5 = "MD5";
    /**
     * 类型 {@value}
     */
    String TYPE_RSA = "RSA";

    /**
     * 超时时效，超过此时间认为签名过期
     */
    Long EXPIRE_TIME = 5 * 60 * 1000L;

    /**
     * 随机数长度
     */
    Integer NONCE_LENGTH = 10;

    /**
     * 属性路径前缀{@value}
     */
    String PROPERTY_PACKAGE_PREFIX = "boot.admin.filter.encryption.api";
}
