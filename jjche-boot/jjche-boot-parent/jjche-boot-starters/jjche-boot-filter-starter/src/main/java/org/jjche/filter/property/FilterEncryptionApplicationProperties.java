package org.jjche.filter.property;

import lombok.Data;

/**
 * <p>
 * app信息
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-14
 */
@Data
public class FilterEncryptionApplicationProperties {
    /**
     * appId
     */
    private String id;
    /**
     * appKey
     */
    private String key;
    /**
     * app私钥
     */
    private String privateKey;
    /**
     * app公钥
     */
    private String publicKey;
}
