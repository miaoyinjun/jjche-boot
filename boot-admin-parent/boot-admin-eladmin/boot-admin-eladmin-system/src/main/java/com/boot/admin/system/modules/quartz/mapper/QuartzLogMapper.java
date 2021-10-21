package com.boot.admin.system.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.system.modules.quartz.domain.QuartzLogDO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>QuartzLogMapper interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-01-07
 * @version 1.0.8-SNAPSHOT
 */
public interface QuartzLogMapper extends MyBaseMapper<QuartzLogDO> {
    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<QuartzLogDO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    List<QuartzLogDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
