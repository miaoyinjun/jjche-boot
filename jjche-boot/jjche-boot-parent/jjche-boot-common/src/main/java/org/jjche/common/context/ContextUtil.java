package org.jjche.common.context;

import cn.hutool.core.convert.Convert;
import com.alibaba.ttl.TransmittableThreadLocal;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.util.StrUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>
 * 获取当前线程变量中的 用户信息
 * --参与lamp-util
 * </p>
 *
 * @author miaoyj
 * @since 2022-08-11
 */
public final class ContextUtil {
    private static final ThreadLocal<Map<String, String>> THREAD_LOCAL = new TransmittableThreadLocal<>();

    private ContextUtil() {
    }

    /**
     * <p>
     * 获取应用加密密钥
     * </p>
     *
     * @return /
     */
    public static String getAppKeyEncKey() {
        return get(SecurityConstant.APP_KEY_ENC_KEY, String.class, StrUtil.EMPTY);
    }

    /**
     * <p>
     * 设置应用加密密钥
     * </p>
     *
     * @param val 加密密钥
     */
    public static void setAppKeyEncKey(String val) {
        set(SecurityConstant.APP_KEY_ENC_KEY, val);
    }

    public static void putAll(Map<String, String> map) {
        map.forEach((k, v) -> {
            set(k, v);
        });
    }

    public static void set(String key, Object value) {
        Map<String, String> map = getLocalMap();
        map.put(key, value == null ? StrUtil.EMPTY : value.toString());
    }

    public static <T> T get(String key, Class<T> type) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.get(key));
    }

    public static <T> T get(String key, Class<T> type, Object def) {
        Map<String, String> map = getLocalMap();
        return Convert.convert(type, map.getOrDefault(key, String.valueOf(def == null ? StrUtil.EMPTY : def)));
    }

    public static String get(String key) {
        Map<String, String> map = getLocalMap();
        return map.getOrDefault(key, StrUtil.EMPTY);
    }

    public static Map<String, String> getLocalMap() {
        Map<String, String> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>(10);
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(Map<String, String> localMap) {
        THREAD_LOCAL.set(localMap);
    }

    private static boolean isEmptyLong(String key) {
        String val = getLocalMap().get(key);
        return val == null || StrUtil.NULL.equals(val) || StrUtil.ZERO.equals(val);
    }

    private static boolean isEmptyStr(String key) {
        String val = getLocalMap().get(key);
        return val == null || StrUtil.NULL.equals(val);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}