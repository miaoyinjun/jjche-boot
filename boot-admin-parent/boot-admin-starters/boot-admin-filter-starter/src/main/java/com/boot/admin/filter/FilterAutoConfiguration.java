package com.boot.admin.filter;

import com.boot.admin.filter.encryption.api.FilterEncryptionAutoConfiguration;
import com.boot.admin.filter.encryption.field.EncryptFieldAop;
import com.boot.admin.filter.encryption.field.EncryptFieldUtil;
import com.boot.admin.filter.encryption.limit.LimitAspect;
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
@Import({FilterEncryptionAutoConfiguration.class, LimitAspect.class,
        EncryptFieldAop.class, EncryptFieldUtil.class})
public class FilterAutoConfiguration {

}
