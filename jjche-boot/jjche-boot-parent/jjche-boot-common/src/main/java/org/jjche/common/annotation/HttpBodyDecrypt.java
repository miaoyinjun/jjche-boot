package org.jjche.common.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * body解密注解
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-05
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpBodyDecrypt {
}
