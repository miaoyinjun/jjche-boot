package com.boot.admin.security.property;

import lombok.Data;

/**
 * <p>
 * 密码加密传输，前端公钥加密，后端私钥解密
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-18
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class SecurityRsaProperties {
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 私钥
     */
    private String privateKey;
}
