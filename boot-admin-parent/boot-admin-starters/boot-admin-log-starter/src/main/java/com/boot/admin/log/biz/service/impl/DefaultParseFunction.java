package com.boot.admin.log.biz.service.impl;


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
public class DefaultParseFunction implements IParseFunction<String> {
    /** {@inheritDoc} */
    @Override
    public String functionName() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String apply(String value) {
        return null;
    }
}
