package com.boot.admin.common.annotation;

import com.boot.admin.common.enums.LogType;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>BizLog class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizLog {
    String value() default "";

    /**
     * 是否保存参数
     *
     * @return isSaveParams
     */
    boolean isSaveParams() default true;

    LogType type() default LogType.SELECT;

    /**
     * 模块
     */
    String module() default "";;

    /**
     * 方法
     */
    String methods() default "";

    /**
     * 查询的bean名称
     */
    Class serviceClass() default BizLog.class;

    /**
     * 查询单个详情的bean的方法
     */
    String queryMethod() default "getById";

    /**
     * 从页面参数中解析出要查询的id，
     * 如域名修改中要从参数中获取customerDomainId的值进行查询
     */
    String parameterKey() default "id";

    /**
     * 是否保存修改前内容
     */
    boolean isSaveBeforeData() default false;

    /**
     * 主键描述
     */
    String parameterKeyDesc() default "主键ID";

    /**
     * 参数主键类型
     */
    Class parameterKeyType() default Serializable.class;

}
