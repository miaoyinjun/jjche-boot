package com.boot.admin.common.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;


/**
 * <p>
 * 安全字段注解
 * 加在需要加密/解密的字段上
 * </p>
 *
 * @author miaoyj
 * @since 2021-08-24
 * @version 1.0.0-SNAPSHOT
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface EncryptField {
}
