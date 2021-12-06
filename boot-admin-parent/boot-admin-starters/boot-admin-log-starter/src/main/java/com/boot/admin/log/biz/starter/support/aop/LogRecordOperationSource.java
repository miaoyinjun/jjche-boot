package com.boot.admin.log.biz.starter.support.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import com.boot.admin.log.biz.beans.LogRecord;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * <p>
 * 配置日志注解与日志记录关系
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class LogRecordOperationSource {


    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param method 方法
     * @param targetClass 目的类
     * @return 日志对象
     */
    public LogRecord computeLogRecordOperation(Method method, Class<?> targetClass) {
        // Don't allow no-public methods as required.
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        // The method may be on an interface, but we need attributes from the target class.
        // If the target class is null, the method will be unchanged.
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

        // First try is the method in the target class.
        return parseLogRecordAnnotation(specificMethod);
    }

    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param ae 日志注解
     * @return 日志对象
     */
    private LogRecord parseLogRecordAnnotation(AnnotatedElement ae) {
        LogRecordAnnotation logRecordAnnotation = AnnotationUtil.getAnnotation(ae, LogRecordAnnotation.class);
        return logRecordAnnotation == null ? null : parseLogRecordAnnotation(ae, logRecordAnnotation);
    }

    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param ae 日志注解元素
     * @param recordAnnotation 日志注解
     * @return 日志对象
     */
    private LogRecord parseLogRecordAnnotation(AnnotatedElement ae, LogRecordAnnotation recordAnnotation) {
        String bizKey = recordAnnotation.prefix();
        String bizNo = recordAnnotation.bizNo();
        LogRecord recordOps = LogRecord.builder()
                .bizKey(bizKey)
                .bizNo(bizNo)
                .operatorId(recordAnnotation.operatorId())
                .category(recordAnnotation.category())
                .value(recordAnnotation.value())
                .type(recordAnnotation.type())
                .module(recordAnnotation.module())
                .detail(recordAnnotation.detail())
                .condition(recordAnnotation.condition())
                .isSaveParams(recordAnnotation.isSaveParams())
                .build();
        return recordOps;
    }

}
