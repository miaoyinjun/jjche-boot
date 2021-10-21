package com.boot.admin.common.util;

import cn.hutool.core.lang.Assert;

/**
 * 验证工具
 *
 * @author Zheng Jie
 * @since 2018-11-23
 * @version 1.0.8-SNAPSHOT
 */
public class ValidationUtil{

    /**
     * 验证空
     *
     * @param obj a {@link java.lang.Object} object.
     * @param entity a {@link java.lang.String} object.
     * @param parameter a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public static void isNull(Object obj, String entity, String parameter , Object value) {
        String msg = entity + " 不存在: " + parameter + " is " + value;
        Assert.notNull(obj, msg);
    }

}
