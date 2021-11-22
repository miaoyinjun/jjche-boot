package com.boot.admin.core.permission;

import java.util.function.Function;

/**
 * <p>
 * 支持过滤
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-11
 * @version 1.0.10-SNAPSHOT
 */
public interface DataPermissionFieldFilterable<T> {

    /**
     * 遍历列表，执行过滤方法
     *
     * @param filterFunc 过滤方法
     */
    void doFilter(Function<T, T> filterFunc);

    Iterable<T> getData();
}
