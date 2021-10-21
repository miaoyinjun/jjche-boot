package com.boot.admin.log.biz.service.impl;

import cn.hutool.core.map.MapUtil;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.log.biz.service.IParseFunction;
import org.springframework.util.CollectionUtils;

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
    private Map<String, IParseFunction> functionMap;

    /**
     * <p>Constructor for ParseFunctionFactory.</p>
     *
     * @param parseFunctions a {@link java.util.List} object.
     */
    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        functionMap = MapUtil.newHashMap();
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        for (IParseFunction parseFunction : parseFunctions) {
            if (StrUtil.isEmpty(parseFunction.functionName())) {
                continue;
            }
            functionMap.put(parseFunction.functionName(), parseFunction);
        }
    }

    /**
     * <p>getFunction.</p>
     *
     * @param functionName a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.log.biz.service.IParseFunction} object.
     */
    public IParseFunction getFunction(String functionName) {
        return functionMap.get(functionName);
    }
}
