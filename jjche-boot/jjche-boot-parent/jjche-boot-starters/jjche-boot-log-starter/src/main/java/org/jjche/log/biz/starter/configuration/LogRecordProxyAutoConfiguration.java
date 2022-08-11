package org.jjche.log.biz.starter.configuration;

import cn.hutool.log.StaticLog;
import org.jjche.log.biz.service.IFunctionService;
import org.jjche.log.biz.service.ILogRecordService;
import org.jjche.log.biz.service.IOperatorGetService;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.*;
import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.jjche.log.biz.starter.diff.DefaultDiffItemsToLogContentService;
import org.jjche.log.biz.starter.diff.IDiffItemsToLogContentService;
import org.jjche.log.biz.starter.support.aop.BeanFactoryLogRecordAdvisor;
import org.jjche.log.biz.starter.support.aop.LogRecordInterceptor;
import org.jjche.log.biz.starter.support.aop.LogRecordOperationSource;
import org.jjche.log.biz.starter.support.parse.LogFunctionParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

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
@EnableConfigurationProperties({LogRecordProperties.class})
public class LogRecordProxyAutoConfiguration implements ImportAware {
    private AnnotationAttributes enableLogRecord;

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordOperationSource logRecordOperationSource() {
        return new LogRecordOperationSource();
    }

    @Bean
    @ConditionalOnMissingBean(IFunctionService.class)
    public IFunctionService functionService(ParseFunctionFactory parseFunctionFactory) {
        return new DefaultFunctionServiceImpl(parseFunctionFactory);
    }

    @Bean
    public ParseFunctionFactory parseFunctionFactory(@Autowired List<IParseFunction> parseFunctions) {
        return new ParseFunctionFactory(parseFunctions);
    }

    @Bean
    @ConditionalOnMissingBean(IParseFunction.class)
    public DefaultParseFunction parseFunction() {
        return new DefaultParseFunction();
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryLogRecordAdvisor logRecordAdvisor(IFunctionService functionService, DiffParseFunction diffParseFunction) {
        BeanFactoryLogRecordAdvisor advisor =
                new BeanFactoryLogRecordAdvisor();
        advisor.setLogRecordOperationSource(logRecordOperationSource());
        advisor.setAdvice(logRecordInterceptor(functionService, diffParseFunction));
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogRecordInterceptor logRecordInterceptor(IFunctionService functionService, DiffParseFunction diffParseFunction) {
        LogRecordInterceptor interceptor = new LogRecordInterceptor();
        interceptor.setLogRecordOperationSource(logRecordOperationSource());
        interceptor.setTenant(enableLogRecord.getString("tenant"));
        interceptor.setLogFunctionParser(logFunctionParser(functionService));
        interceptor.setDiffParseFunction(diffParseFunction);
        return interceptor;
    }

    @Bean
    public LogFunctionParser logFunctionParser(IFunctionService functionService) {
        return new LogFunctionParser(functionService);
    }

    @Bean
    public DiffParseFunction diffParseFunction(IDiffItemsToLogContentService diffItemsToLogContentService) {
        DiffParseFunction diffParseFunction = new DiffParseFunction();
        diffParseFunction.setDiffItemsToLogContentService(diffItemsToLogContentService);
        return diffParseFunction;
    }

    @Bean
    @ConditionalOnMissingBean(IDiffItemsToLogContentService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IDiffItemsToLogContentService diffItemsToLogContentService(IFunctionService functionService, LogRecordProperties logRecordProperties) {
        return new DefaultDiffItemsToLogContentService(functionService, logRecordProperties);
    }

    @Bean
    @ConditionalOnMissingBean(IOperatorGetService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public IOperatorGetService operatorGetService() {
        return new DefaultOperatorGetServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean(ILogRecordService.class)
    @Role(BeanDefinition.ROLE_APPLICATION)
    public ILogRecordService recordService() {
        return new DefaultLogRecordServiceImpl();
    }

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLogRecord = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableLogRecord.class.getName(), false));
        if (this.enableLogRecord == null) {
            StaticLog.info("@EnableCaching is not present on importing class");
        }
    }
}
