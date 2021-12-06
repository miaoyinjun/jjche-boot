package com.boot.admin.log.biz.service.impl;

import com.boot.admin.log.biz.service.IParseFunction;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 收集表达式方法
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class ParseFunctionFactory {
    private Map<String, IParseFunction> allFunctionMap;

    /**
     * <p>
     * 所有方法
     * </p>
     *
     * @param parseFunctions /
     * @return /
     */
    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        allFunctionMap = new HashMap<>();
        for (IParseFunction parseFunction : parseFunctions) {
            if (StringUtils.isEmpty(parseFunction.functionName())) {
                continue;
            }
            allFunctionMap.put(parseFunction.functionName(), parseFunction);
        }
    }

    /**
     * <p>
     * 获取方法
     * </p>
     *
     * @param functionName 方法名
     * @return /
     */
    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

    /**
     * <p>
     * 是否方法前执行
     * </p>
     *
     * @param functionName 方法名
     * @return /
     */
    public boolean isBeforeFunction(String functionName) {
        return allFunctionMap.get(functionName) != null && allFunctionMap.get(functionName).executeBefore();
    }
}