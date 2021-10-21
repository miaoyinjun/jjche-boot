package com.boot.admin.system.modules.version.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.system.modules.version.domain.VersionDO;
import com.boot.admin.system.modules.version.vo.VersionVO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
public interface VersionMapper extends MyBaseMapper<VersionDO> {
    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<VersionVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
