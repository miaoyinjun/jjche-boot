package com.boot.admin.log.biz.starter.annotation;

import com.boot.admin.log.biz.starter.support.LogRecordConfigureSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * 激活日志模块
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogRecordConfigureSelector.class)
public @interface EnableLogRecord {

    /**
     * <p>
     * 租户
     * </p>
     *
     * @return /
     */
    String tenant();

    /**
     * <p>
     * 代理方式
     * </p>
     *
     * @return /
     */
    AdviceMode mode() default AdviceMode.PROXY;
}
