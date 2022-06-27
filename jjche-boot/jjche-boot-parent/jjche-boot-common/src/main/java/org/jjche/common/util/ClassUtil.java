package org.jjche.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * class工具类
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-20
 */
public final class ClassUtil extends cn.hutool.core.util.ClassUtil {
    private static final Set<Class<?>> PRIMITIVE_ARRAY_SET = new HashSet<>();
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_ARRAY_SET = new HashSet<>();

    static {
        // PRIMITIVE_ARRAY_SET
        PRIMITIVE_ARRAY_SET.add(boolean[].class);
        PRIMITIVE_ARRAY_SET.add(byte[].class);
        PRIMITIVE_ARRAY_SET.add(char[].class);
        PRIMITIVE_ARRAY_SET.add(short[].class);
        PRIMITIVE_ARRAY_SET.add(int[].class);
        PRIMITIVE_ARRAY_SET.add(float[].class);
        PRIMITIVE_ARRAY_SET.add(long[].class);
        PRIMITIVE_ARRAY_SET.add(double[].class);
        // PRIMITIVE_WRAPPER_ARRAY_SET
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Boolean[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Byte[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Character[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Short[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Integer[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Float[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Long[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Double[].class);
        PRIMITIVE_WRAPPER_ARRAY_SET.add(Void[].class);
    }

    /**
     * 判断是否为基础或包装类型数组
     *
     * @param clazz 类型
     */
    public static boolean isPrimitiveOrWrapperArray(Class<?> clazz) {
        return PRIMITIVE_ARRAY_SET.contains(clazz) || PRIMITIVE_WRAPPER_ARRAY_SET.contains(clazz);
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        return PRIMITIVE_ARRAY_SET.contains(clazz);
    }

    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        return PRIMITIVE_WRAPPER_ARRAY_SET.contains(clazz);
    }

    public static boolean isBooleanArray(Class<?> clazz) {
        return int[].class.isAssignableFrom(clazz);
    }

    public static boolean isByteArray(Class<?> clazz) {
        return byte[].class.isAssignableFrom(clazz);
    }

    public static boolean isCharArray(Class<?> clazz) {
        return char[].class.isAssignableFrom(clazz);
    }

    public static boolean isShortArray(Class<?> clazz) {
        return short[].class.isAssignableFrom(clazz);
    }

    public static boolean isIntArray(Class<?> clazz) {
        return int[].class.isAssignableFrom(clazz);
    }

    public static boolean isFloatArray(Class<?> clazz) {
        return float[].class.isAssignableFrom(clazz);
    }

    public static boolean isLongArray(Class<?> clazz) {
        return long[].class.isAssignableFrom(clazz);
    }

    public static boolean isDoubleArray(Class<?> clazz) {
        return double[].class.isAssignableFrom(clazz);
    }
}
