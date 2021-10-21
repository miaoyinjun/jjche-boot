package com.boot.admin.property;

import lombok.Data;

/**
 * <p>
 * 密码配置属性类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2021-01-05
 */
@Data
public class PasswordProperties {
    /**
     * 是否激活
     */
    private Boolean enabled;
    /**
     * 密码最小长度
     */
    private String minLength;
    /**
     * 密码最大长度
     */
    private String maxLength;
    /**
     * 默认密码
     */
    private String defaultVal;
    /**
     * 密码连续错误次数
     */
    private Integer failsMaxCount;
    /**
     * 提前多少天设置必须设置密码
     */
    private Integer advanceDayMustReset;
    /**
     * 提前多少天设置提示设置密码
     */
    private Integer advanceDayTipReset;
    /**
     * 多少天密码过期
     */
    private Integer expiredDays;
}
