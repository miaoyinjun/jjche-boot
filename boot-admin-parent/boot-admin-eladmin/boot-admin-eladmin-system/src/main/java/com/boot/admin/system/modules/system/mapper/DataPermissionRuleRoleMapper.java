package com.boot.admin.system.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.system.api.enums.DataPermissionRuleRoleSortEnum;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleRoleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionRuleRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据规则权限
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 * @version 1.0.1-SNAPSHOT
 */
public interface DataPermissionRuleRoleMapper extends MyBaseMapper<DataPermissionRuleRoleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page 分页
     * @param sort 排序
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<DataPermissionRuleRoleVO> pageQuery(PageParam page, DataPermissionRuleRoleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param sort 排序
     * @param wrapper 自定义sql
     * @return DO
     */
    List<DataPermissionRuleRoleDO> queryAll(DataPermissionRuleRoleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
}
