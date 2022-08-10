package org.jjche.filter;

import org.jjche.filter.enc.api.EncAutoConfiguration;
import org.jjche.filter.enc.field.EncryptFieldAop;
import org.jjche.filter.enc.field.EncryptFieldUtil;
import org.jjche.filter.enc.limit.LimitAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Configuration
@Import({LimitAspect.class,
        EncryptFieldAop.class, EncryptFieldUtil.class, EncAutoConfiguration.class})
public class FilterAutoConfiguration {

}
