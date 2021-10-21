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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 解析需要存储的日志里面的SpeEL表达式
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class LogRecordValueParser implements BeanFactoryAware {

    protected BeanFactory beanFactory;

    private LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();
    private IFunctionService functionService;
    private static Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    /**
     * <p>
     * 解析日志表达式模板
     * </p>
     *
     * @param templates 模板
     * @param ret 结果
     * @param targetClass 目的类
     * @param method 方法
     * @param args 对象
     * @param errorMsg 错误 信息
     * @return map
     */
    public Map<String, String> processTemplate(Collection<String> templates, Object ret, Class<?> targetClass, Method method, Object[] args, String errorMsg) {
        Map<String, String> expressionValues = MapUtil.newHashMap();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);
        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{{") || expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    Object value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    String functionName = matcher.group(1);
                    matcher.appendReplacement(parsedStr, StrUtil.nullToEmpty(functionService.apply(functionName, value)));
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

    /** {@inheritDoc} */
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
