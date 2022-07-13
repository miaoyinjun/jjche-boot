package org.jjche.core.base;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * pojo类型转换
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-12
 */
public interface BaseVoMapStruct<E, V> {
    /**
     * <p>
     * DO转VO
     * </p>
     *
     * @param doo DO
     * @return VO
     */
    V toVO(E doo);

    /**
     * <p>
     * DO集合转VO集合
     * </p>
     *
     * @param dooList DOList
     * @return VOList
     */
    List<V> toVO(Collection<E> dooList);
}