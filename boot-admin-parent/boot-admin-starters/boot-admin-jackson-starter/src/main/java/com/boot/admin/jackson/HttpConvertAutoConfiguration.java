package com.boot.admin.jackson;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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
public class HttpConvertAutoConfiguration extends WebMvcConfigurationSupport {

    /** {@inheritDoc} */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(new JacksonHttpMessageConverter());
    }
}
