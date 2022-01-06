package org.jjche.log.biz.starter.configuration;

import cn.hutool.log.StaticLog;
import org.jjche.log.biz.service.IFunctionService;
import org.jjche.log.biz.service.ILogRecordService;
import org.jjche.log.biz.service.IOperatorGetService;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.*;
import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.jjche.log.biz.starter.support.aop.BeanFactoryLogRecordAdvisor;
import org.jjche.log.biz.starter.support.aop.LogRecordInterceptor;
import org.jjche.log.biz.starter.support.aop.LogRecordOperationSource;
import org.jjche.log.filter.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 日志配置入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
@Configuration
public class LogRecordProxyAutoConfiguration implements ImportAware {

    private AnnotationAttributes enableLogRecord;

    /**
     * <p>
     * 日志拦截
     * </p>
     *
     * @return 日志拦截配置
     */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            /**
             * 添加拦截器
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LogInterceptor());
            }
        };
    }

    /**
     * <p>logRecordOperationSource.</p>
     *
     * @return a {@link LogRecordOperationSource} object.
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordOperationSource logRecordOperationSource() {
        return new LogRecordOperationSource();
    }

    /**
     * <p>functionService.</p>
     *
     * @param parseFunctionFactory a {@link ParseFunctionFactory} object.
     * @return a {@link IFunctionService} object.
     */
    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    /**
     * <p>parseFunctionFactory.</p>
     *
     * @param parseFunctions a {@link java.util.List} object.
     * @return a {@link ParseFunctionFactory} object.
     */
    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    /**
     * <p>parseFunction.</p>
     *
     * @return a {@link DefaultParseFunction} object.
     */
    @Bean
    @ConditionalOnMissingBean(IParseFunction.class)
    public DefaultParseFunction parseFunction() {
        return new DefaultParseFunction();
    }


    /**
     * <p>logRecordAdvisor.</p>
     *
     * @param functionService a {@link IFunctionService} object.
     * @return a {@link BeanFactoryLogRecordAdvisor} object.
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLogRecordAdvisor logRecordAdvisor(IFunctionService functionService) {
        BeanFactoryLogRecordAdvisor advisor =
                new BeanFactoryLogRecordAdvisor();
        advisor.setLogRecordOperationSource(logRecordOperationSource());
        advisor.setAdvice(logRecordInterceptor(functionService));
        return advisor;
    }

    /**
     * <p>logRecordInterceptor.</p>
     *
     * @param functionService a {@link IFunctionService} object.
     * @return a {@link LogRecordInterceptor} object.
     */
    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordInterceptor logRecordInterceptor(IFunctionService functionService) {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordOperationSource(logRecordOperationSource());
        interceptor.setTenant(enableLogRecord.getString("tenant"));
        interceptor.setFunctionService(functionService);
        return interceptor;
    }

    /**
     * <p>operatorGetService.</p>
     *
     * @return a {@link IOperatorGetService} object.
     */
    @Bean
    @ConditionalOnMissingBean(IOperatorGetService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IOperatorGetService operatorGetService() {
        return new DefaultOperatorGetServiceImpl();
    }

    /**
     * <p>recordService.</p>
     *
     * @return a {@link ILogRecordService} object.
     */
    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService recordService() {
        return new DefaultLogRecordServiceImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLogRecord = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
        if (this.enableLogRecord == null) {
            StaticLog.info("@EnableCaching is not present on importing class");
        }
    }
}
