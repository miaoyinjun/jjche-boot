package com.boot.admin.system.modules.quartz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.system.modules.quartz.domain.QuartzJobDO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>QuartzJobMapper interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
public interface QuartzJobMapper extends MyBaseMapper<QuartzJobDO> {
    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<QuartzJobDO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    List<QuartzJobDO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
