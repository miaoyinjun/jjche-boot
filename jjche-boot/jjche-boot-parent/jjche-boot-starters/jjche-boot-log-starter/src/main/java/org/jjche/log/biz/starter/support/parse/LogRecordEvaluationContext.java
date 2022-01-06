package org.jjche.log.biz.starter.support.parse;

import org.jjche.log.biz.context.LogRecordContext;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>
 * 日志表达式变量设置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordEvaluationContext extends MethodBasedEvaluationContext {

    /**
     * <p>Constructor for LogRecordEvaluationContext.</p>
     *
     * @param rootObject              a {@link java.lang.Object} object.
     * @param method                  a {@link java.lang.reflect.Method} object.
     * @param arguments               an array of {@link java.lang.Object} objects.
     * @param parameterNameDiscoverer a {@link org.springframework.core.ParameterNameDiscoverer} object.
     * @param ret                     a {@link java.lang.Object} object.
     * @param errorMsg                a {@link java.lang.String} object.
     */
    public LogRecordEvaluationContext(Object rootObject, Method method, Object[] arguments,
                                      ParameterNameDiscoverer parameterNameDiscoverer, Object ret, String errorMsg) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
        Map<String, Object> variables = LogRecordContext.getVariables();
        if (variables != null && variables.size() > 0) {
            for (Map.Entry<String, Object> entry : variables.entrySet()) {
                setVariable(entry.getKey(), entry.getValue());
            }
        }
        setVariable("_ret", ret);
        setVariable("_errorMsg", errorMsg);
    }
}
