package org.jjche.log.biz.context;


import cn.hutool.core.map.MapUtil;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

/**
 * <p>
 * 日志临时变量
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordContext {

    private static final InheritableThreadLocal<Deque<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    /**
     * <p>putVariable.</p>
     *
     * @param name  a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     */
    public static void putVariable(String name, Object value) {
        if (variableMapStack.get() == null) {
            Deque<Map<String, Object>> stack = new ArrayDeque<>();
            variableMapStack.set(stack);
        }
        Deque<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack.size() == 0) {
            variableMapStack.get().push(MapUtil.newHashMap());
        }
        variableMapStack.get().element().put(name, value);
    }

    /**
     * <p>getVariable.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public static Object getVariable(String key) {
        Map<String, Object> variableMap = variableMapStack.get().peek();
        return variableMap == null ? null : variableMap.get(key);
    }

    /**
     * <p>getVariables.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public static Map<String, Object> getVariables() {
        Deque<Map<String, Object>> mapStack = variableMapStack.get();
        return mapStack.peek();
    }

    /**
     * <p>clear.</p>
     */
    public static void clear() {
        if (variableMapStack.get() != null) {
            variableMapStack.get().pop();
        }
    }

    /**
     * 日志使用方不需要使用到这个方法
     * 每进入一个方法初始化一个 span 放入到 stack中，方法执行完后 pop 掉这个span
     */
    public static void putEmptySpan() {
        Deque<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack == null) {
            Deque<Map<String, Object>> stack = new ArrayDeque<>();
            variableMapStack.set(stack);
        }
        variableMapStack.get().push(MapUtil.newHashMap());
    }
}
