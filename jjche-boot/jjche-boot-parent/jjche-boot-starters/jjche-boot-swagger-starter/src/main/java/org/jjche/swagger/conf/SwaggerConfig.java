package org.jjche.swagger.conf;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.enums.FilterEncryptionEnum;
import org.jjche.swagger.property.SwaggerSecurityJwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * <p>
 * Swagger配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class SwaggerConfig implements EnvironmentAware {
    @Autowired
    private SwaggerProperties swaggerProperty;
    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    /**
     * <p>notRegex.</p>
     *
     * @param pathRegex a {@link java.lang.String} object.
     * @return a {@link java.util.function.Predicate} object.
     */
    public static Predicate<String> notRegex(final String pathRegex) {
        return new Predicate<String>() {
            @Override
            public boolean test(String input) {
                return !input.matches(pathRegex);
            }
        };
    }

    /**
     * <p>swaggerApi</p>
     *
     * @return a {@link springfox.documentation.spring.web.plugins.Docket} object.
     */
    @SuppressWarnings("unchecked")
    @Bean
    public Docket swaggerApi() {
        String defaultGroupName = "default";
        String ignoreFilterPath = this.ignoreFilterPath();
        List<Parameter> operationParameters = CollUtil.newArrayList(encryptionHeaders());
        ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(backendApiInfo())
                .groupName(defaultGroupName)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperty.getBasePackage()))
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(defaultGroupName))
                .securityContexts(CollectionUtil.newArrayList(securityContext()))
                .securitySchemes(CollectionUtil.newArrayList(apiKey()))
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .pathMapping("/")
                .select();
        //过滤的接口
        select.paths(PathSelectors.regex(swaggerProperty.getFilterPath() + "/.*"))
                .paths(notRegex(ignoreFilterPath));
        return select.build().globalOperationParameters(operationParameters);
    }

    private ApiInfo backendApiInfo() {
        return new ApiInfo(swaggerProperty.getTitle(),
                swaggerProperty.getDescription(), swaggerProperty.getVersion(), "",
                swaggerProperty.getContact(), null, null, new ArrayList<VendorExtension>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnvironment(Environment environment) {
        Contact contact = new Contact(swaggerProperty.getContactName(), swaggerProperty.getContactUrl(), swaggerProperty.getContactEmail());
        swaggerProperty.setContact(contact);
    }

    /**
     * <p>
     * 认证header
     * </p>
     *
     * @return apiKey
     * @author miaoyj
     * @since 2020-08-12
     */
    private ApiKey apiKey() {
        SwaggerSecurityJwtProperties swaggerPropertySecurityJwt = swaggerProperty.getSecurityJwt();
        String tokenHeader = swaggerPropertySecurityJwt.getTokenHeader();
        return new ApiKey(tokenHeader, tokenHeader, "header");
    }

    /**
     * <p>
     * 接口header读取apiKey，加锁标记
     * </p>
     *
     * @return
     * @author miaoyj
     * @since 2020-11-26
     */
    private SecurityContext securityContext() {
        //获取Spring Security里忽略的url
        Set<String> anonymousUrls = SecurityConstant.ANONYMOUS_URLS;
        //@AnonymousGetMapping("/{id}") 启动失败问题，但无法删除锁标记
        anonymousUrls.removeIf(obj -> obj.contains("{") || obj.contains("}"));
        String notPathRegex = StrUtil.join("|", anonymousUrls);
        //匹配替换正确的格式
        notPathRegex = notPathRegex.replaceAll("\\*\\*", "*");
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(notRegex(notPathRegex))
                //.forPaths(PathSelectors.regex(".*?208.*$"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        SwaggerSecurityJwtProperties swaggerPropertySecurityJwt = swaggerProperty.getSecurityJwt();
        String tokenHeader = swaggerPropertySecurityJwt.getTokenHeader();

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return CollectionUtil.newArrayList(new SecurityReference(tokenHeader, authorizationScopes));
    }

    /**
     * <p>
     * 添加加密过滤器所有字段
     * </p>
     *
     * @return header
     * @author miaoyj
     * @since 2020-08-12
     */
    private List<Parameter> encryptionHeaders() {
        List<Parameter> pars = new ArrayList<>();
        if (swaggerProperty.isEncryptionEnabled()) {
            for (FilterEncryptionEnum eEnum : FilterEncryptionEnum.values()) {
                ParameterBuilder par = new ParameterBuilder();
                par.name(eEnum.getKey()).description(eEnum.getDes())
                        .modelRef(new ModelRef("string")).parameterType("header")
                        .required(true).build();
                pars.add(par.build());
            }
        }
        return pars;
    }

    /**
     * <p>
     * 添加token认证字段
     * </p>
     *
     * @return header
     * @author miaoyj
     * @since 2020-08-12
     */
    private List<Parameter> securityHeaders() {
        List<Parameter> pars = new ArrayList<>();
        SwaggerSecurityJwtProperties swaggerPropertySecurityJwt = swaggerProperty.getSecurityJwt();
        if (swaggerPropertySecurityJwt.isEnabled()) {
            ParameterBuilder par = new ParameterBuilder();
            par.name(swaggerPropertySecurityJwt.getTokenHeader())
                    .modelRef(new ModelRef("string")).parameterType("header")
                    .defaultValue(swaggerPropertySecurityJwt.getTokenStartWith() + " ")
                    .required(true).build();
            pars.add(par.build());
        }
        return pars;
    }

    /**
     * <p>
     * 获取要忽略的API前缀
     * </p>
     *
     * @return 正则
     * @author miaoyj
     * @since 2020-09-22
     */
    private String ignoreFilterPath() {
        List<String> ignoreFilterPath = swaggerProperty.getIgnoreFilterPath();
        return StrUtil.join("|", ignoreFilterPath);
    }


    /**
     * 增加如下配置可解决Spring Boot 6.x 与Swagger 3.0.0 不兼容问题
     **/
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
