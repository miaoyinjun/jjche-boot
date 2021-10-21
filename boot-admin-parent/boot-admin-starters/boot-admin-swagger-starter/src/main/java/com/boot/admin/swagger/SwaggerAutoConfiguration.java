package com.boot.admin.swagger;

import com.boot.admin.swagger.conf.*;
import com.boot.admin.swagger.property.plugin.EnumWebMvcConfiguration;
import com.boot.admin.swagger.property.plugin.SwaggerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({SwaggerProperties.class, SwaggerConfig.class,
        SwaggerWebMvcConfigurer.class, SwaggerController.class,
        BeanValidatorPluginsConfiguration.class,
        CustomizeModelAttributeParameterExpander.class,
        SwaggerConfiguration.class, EnumWebMvcConfiguration.class
})
@Configuration
@EnableSwagger2WebMvc
public class SwaggerAutoConfiguration {}
