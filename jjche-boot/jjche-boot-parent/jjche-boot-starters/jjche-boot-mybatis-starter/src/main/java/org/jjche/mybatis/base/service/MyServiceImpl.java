package org.jjche.mybatis.base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jjche.mybatis.base.MyBaseMapper;

/**
 * <p>
 * 自定义service实现
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-26
 */
public class MyServiceImpl<M extends MyBaseMapper<T>, T> extends ServiceImpl<M, T>
        implements IMyService<T> {
}
