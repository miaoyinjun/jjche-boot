package com.boot.admin.gen.modules.generator.mapper;

import com.boot.admin.gen.modules.generator.domain.GenConfigDO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;

import java.util.List;

/**
 * <p>GenConfigMapper interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-14
 */
public interface GenConfigMapper extends MyBaseMapper<GenConfigDO> {
    /**
     * <p>
     * 分页查询表
     * </p>
     *
     * @param page      分页
     * @param tableName 表名
     * @return /
     */
    MyPage pageTable(PageParam page, String tableName);

    /**
     * <p>
     * 查询表
     * </p>
     *
     * @param tableName 表名
     * @return /
     */
    List queryColumn(String tableName);
}
