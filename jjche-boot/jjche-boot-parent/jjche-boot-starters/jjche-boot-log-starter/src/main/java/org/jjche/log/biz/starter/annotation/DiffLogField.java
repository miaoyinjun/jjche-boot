package org.jjche.log.biz.starter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * 对象比较
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-12
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DiffLogField {

    String name();

    String function() default "";

    //   String dateFormat() default "";
}
