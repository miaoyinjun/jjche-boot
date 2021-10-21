package com.boot.admin.security.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 用户 类型枚举
 * </p>
 *
 * @author miaoyj
 * @since 2021-05-18
 * @version 1.0.0-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    /**
     * 主键
     */
    PWD("PWD", "密码用户"),
    SMS("SMS", "短信用户"),
    ;

    private final String value;
    private final String desc;
}
