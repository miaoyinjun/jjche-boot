package com.boot.admin.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 查询构造
 * </p>
 *
 * @author miaoyj
 * @since 2020-10-10
 * @version 1.0.8-SNAPSHOT
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCriteria {

    /**
     * <p>
     * 基本对象的属性名
     * </p>
     *
     * @return name
     */
    String propName() default "";

    /**
     * <p>
     * 查询方式
     * </p>
     *
     * @return type
     */
    Type type() default Type.EQUAL;

    enum Type {
        //相等
        EQUAL
        //大于等于
        , GREATER_THAN
        //小于等于
        , LESS_THAN
        //中模糊查询
        , INNER_LIKE
        // between
        , BETWEEN
        // 包含
        , IN
        // 不为空
        ,NOT_NULL
        // 为空
        ,IS_NULL
    }

    /**
     * @author Zheng Jie
     * 适用于简单连接查询，复杂的请自定义该注解，或者使用sql查询
     */
    enum Join {
        /**
         * jie 2019-6-4 13:18:30
         */
        LEFT, RIGHT, INNER
    }

}

