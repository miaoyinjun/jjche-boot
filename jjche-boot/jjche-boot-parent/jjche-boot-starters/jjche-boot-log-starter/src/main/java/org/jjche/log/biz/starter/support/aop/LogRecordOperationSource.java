package org.jjche.log.biz.starter.support.aop;

import cn.hutool.core.annotation.AnnotationUtil;
import org.jjche.common.dto.LogRecordDTO;
import org.jjche.log.biz.starter.annotation.LogRecord;
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
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordOperationSource {


    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param method      方法
     * @param targetClass 目的类
     * @return 日志对象
     */
    public LogRecordDTO computeLogRecordOperation(Method method, Class<?> targetClass) {
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
    private LogRecordDTO parseLogRecordAnnotation(AnnotatedElement ae) {
        LogRecord logRecord = AnnotationUtil.getAnnotation(ae, LogRecord.class);
        return logRecord == null ? null : parseLogRecordAnnotation(ae, logRecord);
    }

    /**
     * <p>
     * 转换日志注解与日志对象
     * </p>
     *
     * @param ae               日志注解元素
     * @param recordAnnotation 日志注解
     * @return 日志对象
     */
    private LogRecordDTO parseLogRecordAnnotation(AnnotatedElement ae, LogRecord recordAnnotation) {
        String bizKey = recordAnnotation.prefix();
        String bizNo = recordAnnotation.bizNo();
        LogRecordDTO recordOps = LogRecordDTO.builder()
                .bizKey(bizKey)
                .bizNo(bizNo)
                .operatorId(recordAnnotation.operatorId())
                .category(recordAnnotation.category())
                .value(recordAnnotation.value())
                .type(recordAnnotation.type())
                .module(recordAnnotation.module())
                .subModule(recordAnnotation.subModule())
                .detail(recordAnnotation.detail())
                .condition(recordAnnotation.condition())
                .saveParams(recordAnnotation.saveParams())
                .batch(recordAnnotation.batch())
                .build();
        return recordOps;
    }


}
