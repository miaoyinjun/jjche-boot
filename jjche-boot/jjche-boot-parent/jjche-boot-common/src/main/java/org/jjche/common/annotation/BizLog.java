package org.jjche.common.annotation;

import org.jjche.common.enums.LogType;

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
     * @return saveParams
     */
    boolean saveParams() default true;

    LogType type() default LogType.SELECT;

    /**
     * <p>
     * 模块
     * </p>
     *
     * @return /
     */
    String module() default "";

    /**
     * <p>
     * 方法
     * </p>
     *
     * @return /
     */
    String methods() default "";

    /**
     * <p>
     * 查询的bean名称
     * </p>
     *
     * @return /
     */
    Class serviceClass() default BizLog.class;

    /**
     * <p>
     * 查询单个详情的bean的方法
     * </p>
     *
     * @return /
     */
    String queryMethod() default "getById";

    /**
     * <p>
     * 从页面参数中解析出要查询的id，
     * 如域名修改中要从参数中获取customerDomainId的值进行查询
     * </p>
     *
     * @return /
     */
    String parameterKey() default "id";

    /**
     * <p>
     * 是否保存修改前内容
     * </p>
     *
     * @return /
     */
    boolean isSaveBeforeData() default false;

    /**
     * <p>
     * 主键描述
     * </p>
     *
     * @return /
     */
    String parameterKeyDesc() default "主键ID";

    /**
     * <p>
     * 参数主键类型
     * </p>
     *
     * @return /
     */
    Class parameterKeyType() default Serializable.class;

}
