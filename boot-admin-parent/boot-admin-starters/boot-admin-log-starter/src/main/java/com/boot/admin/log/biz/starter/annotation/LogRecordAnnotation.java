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
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogRecordAnnotation {

    /**
     * <p>
     * 是否保存参数
     * </p>
     *
     * @return /
     */
    boolean isSaveParams() default true;

    /**
     * <p>
     * 描述
     * </p>
     *
     * @return /
     */
    String value() default "";

    /**
     * <p>
     * 操作人id
     * </p>
     *
     * @return /
     */
    String operatorId() default "";

    /**
     * <p>
     * 标识
     * 默认取当前restApi的value值
     * 是拼接在 bizNo 上作为 log 的一个标识。避免 bizNo 都为整数 ID 的时候和其他的业务中的 ID 重复。比如订单 ID、用户 ID 等
     * </p>
     *
     * @return /
     */
    String prefix() default "";

    /**
     * <p>
     * 业务编号
     * 比如订单ID，我们查询的时候可以根据 bizNo 查询和它相关的操作日志
     * </p>
     *
     * @return /
     */
    String bizNo() default "";

    /**
     * <p>
     * 日志分类
     * </p>
     *
     * @return /
     */
    LogCategoryType category() default LogCategoryType.MANAGER;

    /**
     * <p>
     * 操作类型
     * </p>
     *
     * @return /
     */
    LogType type();

    /**
     * <p>
     * 模块
     * </p>
     *
     * @return /
     */
    String module();

    /**
     * <p>
     * 详情
     * </p>
     *
     * @return /
     */
    String detail() default "";

    /**
     * <p>
     * 条件
     * </p>
     *
     * @return /
     */
    String condition() default "";
}
