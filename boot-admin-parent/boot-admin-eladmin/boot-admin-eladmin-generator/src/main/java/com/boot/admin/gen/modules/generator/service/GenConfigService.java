package com.boot.admin.gen.modules.generator.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.admin.gen.modules.generator.domain.GenConfigDO;
import com.boot.admin.gen.modules.generator.mapper.GenConfigMapper;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>GenConfigService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-14
 */
@Service
@RequiredArgsConstructor
public class GenConfigService extends MyServiceImpl<GenConfigMapper, GenConfigDO> {

    /**
     * 查询表配置
     *
     * @param tableName 表名
     * @return 表配置
     */
    public GenConfigDO find(String tableName) {
        LambdaQueryWrapper<GenConfigDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GenConfigDO::getTableName, tableName);
        GenConfigDO genConfig = this.getOne(queryWrapper);
        if (genConfig == null) {
            return new GenConfigDO(tableName);
        }
        return genConfig;
    }

    /**
     * 更新表配置
     *
     * @param genConfig 表配置
     * @return 表配置
     */
    public GenConfigDO update(GenConfigDO genConfig) {
        this.saveOrUpdate(genConfig);
        return genConfig;
    }
}
