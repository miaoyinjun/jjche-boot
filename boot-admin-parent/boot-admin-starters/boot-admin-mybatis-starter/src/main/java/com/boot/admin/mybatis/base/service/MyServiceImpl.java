package com.boot.admin.mybatis.base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.admin.mybatis.base.MyBaseMapper;

/**
 * <p>
 * 自定义service实现
 * </p>
 *
 * @author miaoyj
 * @since 2020-08-26
 * @version 1.0.0-SNAPSHOT
 */
public class MyServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T>
        implements IMyService<T> {
}
