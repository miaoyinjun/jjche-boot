package org.jjche.common.enums;

/**
 * <p>
 * 加密定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-12
 */
public enum FilterEncryptionEnum {
    /**
     * 应用标识
     */
    APP_ID("appId", "应用标识", "参数appId错误"),
    /**
     * 时间戳(毫秒)
     */
    TIMESTAMP("timestamp", "时间戳(毫秒)", "参数timestamp错误"),
    /**
     * 随机数
     */
    NONCE("nonce", "随机数", "参数nonce错误"),
    /**
     * 签名
     */
    SIGN("sign", "签名", "参数sign错误"),
    ;
    /**
     * 标识
     */
    private String key;

    /**
     * 描述
     */
    private String des;

    /**
     * 错误信息
     */
    private String errMsg;

    FilterEncryptionEnum(String key, String des, String errMsg) {
        this.key = key;
        this.des = des;
        this.errMsg = errMsg;
    }

    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>Getter for the field <code>des</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDes() {
        return des;
    }

    /**
     * <p>Getter for the field <code>errMsg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getErrMsg() {
        return errMsg;
    }
}
