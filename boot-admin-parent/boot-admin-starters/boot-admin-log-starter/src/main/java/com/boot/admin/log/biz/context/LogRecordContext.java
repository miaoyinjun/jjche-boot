package com.boot.admin.log.biz.context;


import cn.hutool.core.map.MapUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志临时变量
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class LogRecordContext {

    private static final InheritableThreadLocal<Map<String, Object>> VARIABLE_MAP = new InheritableThreadLocal<>();


    /**
     * <p>putVariable.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public static void putVariable(String name, Object value) {
        if (VARIABLE_MAP.get() == null) {
            HashMap<String, Object> map = MapUtil.newHashMap();
            map.put(name, value);
            VARIABLE_MAP.set(map);
        }
        VARIABLE_MAP.get().put(name, value);
    }

    /**
     * <p>getVariables.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public static Map<String, Object> getVariables() {
        return VARIABLE_MAP.get() == null ? MapUtil.newHashMap() : VARIABLE_MAP.get();
    }

    /**
     * <p>clear.</p>
     */
    public static void clear() {
        if (VARIABLE_MAP.get() != null) {
            VARIABLE_MAP.get().clear();
        }
    }
}
