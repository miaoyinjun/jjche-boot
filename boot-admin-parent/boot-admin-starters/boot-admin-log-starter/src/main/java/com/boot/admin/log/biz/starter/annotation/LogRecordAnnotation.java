package com.boot.admin.log.biz.starter.annotation;

import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;

import java.lang.annotation.*;

/**
 * <p>
 * 日志注解
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {

    /**
     * 是否保存参数
     */
    boolean isSaveParams() default true;

    /**
     * 描述
     */
    String value() default "";

    /**
     * 操作人id
     */
    String operatorId() default "";

    /**
     * 标识
     * 是拼接在 bizNo 上作为 log 的一个标识。避免 bizNo 都为整数 ID 的时候和其他的业务中的 ID 重复。比如订单 ID、用户 ID 等
     */
    String prefix() default "";

    /**
     * 业务编号
     * 比如订单ID，我们查询的时候可以根据 bizNo 查询和它相关的操作日志
     */
    String bizNo() default "";

    /**
     * 日志分类
     */
    LogCategoryType category() default LogCategoryType.MANAGER;

    /**
     * 操作类型
     */
    LogType type();

    /**
     * 模块
     */
    String module();

    /**
     * 详情
     */
    String detail() default "";
}
