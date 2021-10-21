package com.boot.admin.tool.modules.tool.utils;

/**
 * 支付状态
 *
 * @author zhengjie
 * @since 2018/08/01 16:45:43
 * @version 1.0.8-SNAPSHOT
 */
public enum  AliPayStatusEnum {

    /** 交易成功 */
    FINISHED("TRADE_FINISHED"),

    /** 支付成功 */
    SUCCESS("TRADE_SUCCESS"),

    /** 交易创建 */
    BUYER_PAY("WAIT_BUYER_PAY"),

    /** 交易关闭 */
    CLOSED("TRADE_CLOSED");

    private final String value;

    AliPayStatusEnum(String value) {
        this.value = value;
    }

    /**
     * <p>Getter for the field <code>value</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getValue() {
        return value;
    }
}
