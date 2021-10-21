package com.boot.admin.security.permission.field;

import java.lang.annotation.*;

/**
 * <p>
 * 标识此方法为查询方法，可能会受到数据权限控制，理论上所有查询方法都应该加上此注释
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-11
 * @version 1.0.10-SNAPSHOT
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface DataPermissionFieldMethod {
    String value();
}
