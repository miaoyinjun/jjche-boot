package com.boot.admin.mybatis.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.admin.mybatis.base.MyBaseMapper;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-26
 * @version 1.0.0-SNAPSHOT
 */
public interface IMyService<T> extends IService<T> {

    /**
     * {@inheritDoc}
     *
     * <p>
     * 获取当前mapper
     * </p>
     * @return /
     */
    @Override
    MyBaseMapper<T> getBaseMapper();

    /**
     * 逻辑删除并填充字段
     *
     * @param entity DO
     * @return 影响行数
     */
    default int removeByIdWithFill(T entity) {
        return this.getBaseMapper().deleteByIdWithFill(entity);
    }

    /**
     * 批量逻辑删除并填充字段
     *
     * @param entity  DO
     * @param wrapper 条件构造器
     * @return 影响行数
     */
    default int removeBatchWithFill(T entity, Wrapper<T> wrapper) {
        return this.getBaseMapper().deleteBatchWithFill(entity, wrapper);
    }

    /**
     * 批量逻辑删除ids并填充字段
     *
     * @param entity DO
     * @param idList 主键
     * @return 影响行数
     */
    default int removeBatchByIdsWithFill(T entity, Collection<? extends Serializable> idList) {
        return this.getBaseMapper().deleteBatchByIdsWithFill(entity, idList);
    }

}
