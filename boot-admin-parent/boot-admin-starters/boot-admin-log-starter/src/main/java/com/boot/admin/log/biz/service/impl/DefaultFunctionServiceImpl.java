package com.boot.admin.log.biz.service.impl;


import com.boot.admin.log.biz.service.IFunctionService;
import com.boot.admin.log.biz.service.IParseFunction;

/**
 * <p>
 * 缺省表达式方法调用
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class DefaultFunctionServiceImpl implements IFunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    /**
     * <p>Constructor for DefaultFunctionServiceImpl.</p>
     *
     * @param parseFunctionFactory a {@link com.boot.admin.log.biz.service.impl.ParseFunctionFactory} object.
     */
    public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    /** {@inheritDoc} */
    @Override
    public String apply(String functionName, Object value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value.toString();
        }
        return function.apply(value);
    }
}
