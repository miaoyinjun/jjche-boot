package com.boot.admin.log.biz.starter.support.parse;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.boot.admin.log.biz.service.IFunctionService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 解析需要存储的日志里面的SpeEL表达式
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordValueParser implements BeanFactoryAware {

    protected BeanFactory beanFactory;

    private final LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();
    private IFunctionService functionService;
    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    /**
     * <p>
     * 解析日志表达式模板
     * </p>
     *
     * @param templates   模板
     * @param ret         结果
     * @param targetClass 目的类
     * @param method      方法
     * @param args        对象
     * @param errorMsg    错误 信息
     * @return map
     */
    public Map<String, String> processTemplate(Collection<String> templates, Object ret,
                                               Class<?> targetClass, Method method, Object[] args,
                                               String errorMsg, Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> expressionValues = MapUtil.newHashMap();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);
        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    String functionReturnValue = getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, matcher.group(1));
                    matcher.appendReplacement(parsedStr, StrUtil.nullToEmpty(functionReturnValue));
                }
                matcher.appendTail(parsedStr);
                if (StrUtil.isNotBlank(expressionTemplate)) {
                    expressionValues.put(expressionTemplate, parsedStr.toString());
                }
            } else {
                if (StrUtil.isNotBlank(expressionTemplate)) {
                    expressionValues.put(expressionTemplate, expressionTemplate);
                }
            }
        }
        return expressionValues;
    }

    /**
     * <p>
     * 处理方法执行前
     * </p>
     *
     * @param templates 模板
     * @param targetClass 目标类
     * @param method 方法
     * @param args 参数
     * @return /
     */
    public Map<String, String> processBeforeExecuteFunctionTemplate(Collection<String> templates, Class<?> targetClass, Method method, Object[] args) {
        Map<String, String> functionNameAndReturnValueMap = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, null, null, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#_errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String functionName = matcher.group(1);
                    if (functionService.beforeFunction(functionName)) {
                        Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        String functionReturnValue = getFunctionReturnValue(null, value, functionName);
                        functionNameAndReturnValueMap.put(functionName, functionReturnValue);
                    }
                }
            }
        }
        return functionNameAndReturnValueMap;
    }

    private String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, Object value, String functionName) {
        String functionReturnValue = "";
        if (beforeFunctionNameAndReturnMap != null) {
            functionReturnValue = beforeFunctionNameAndReturnMap.get(functionName);
        }
        if (StrUtil.isBlank(functionReturnValue)) {
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * <p>Setter for the field <code>functionService</code>.</p>
     *
     * @param functionService a {@link com.boot.admin.log.biz.service.IFunctionService} object.
     */
    public void setFunctionService(IFunctionService functionService) {
        this.functionService = functionService;
    }
}
