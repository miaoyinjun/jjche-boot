package org.jjche.jackson;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({JacksonHttpMessageConverter.class})
@Configuration
public class JacksonAutoConfiguration {

    /**
     * {@inheritDoc}
     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        super.configureMessageConverters(converters);
//        converters.add(new JacksonHttpMessageConverter());
//    }
}
