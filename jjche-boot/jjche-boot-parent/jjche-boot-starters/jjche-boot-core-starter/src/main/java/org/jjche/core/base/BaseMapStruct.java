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
public interface BaseMapStruct<E, D, V> {

    /**
     * <p>
     * DTO转DO
     * </p>
     *
     * @param dto DTO
     * @return DO
     * @author miaoyj
     * @since 2020-10-12
     */
    E toDO(D dto);

    /**
     * <p>
     * DO转VO
     * </p>
     *
     * @param doo DO
     * @return VO
     * @author miaoyj
     * @since 2020-10-12
     */
    V toVO(E doo);

    /**
     * <p>
     * DTO集合转DO集合
     * </p>
     *
     * @param dtoList dtoList
     * @return DOList
     * @author miaoyj
     * @since 2020-10-12
     */
    List<E> toDO(Collection<D> dtoList);

    /**
     * <p>
     * DO集合转VO集合
     * </p>
     *
     * @param dooList DOList
     * @return VOList
     * @author miaoyj
     * @since 2020-10-12
     */
    List<V> toVO(Collection<E> dooList);
}
