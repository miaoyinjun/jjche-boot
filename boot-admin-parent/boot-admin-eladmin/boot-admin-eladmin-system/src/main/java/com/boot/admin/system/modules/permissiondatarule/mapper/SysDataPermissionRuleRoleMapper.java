package com.boot.admin.system.modules.permissiondatarule.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.permissiondatarule.api.enums.SysDataPermissionRuleRoleSortEnum;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleRoleVO;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* <p>
* 数据规则权限
* </p>
*
* @author miaoyj
* @since 2021-11-01
*/
public interface SysDataPermissionRuleRoleMapper extends MyBaseMapper<SysDataPermissionRuleRoleDO> {

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
    MyPage<SysDataPermissionRuleRoleVO> pageQuery(PageParam page, SysDataPermissionRuleRoleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
    * <p>
    * 查询全部
    * </p>
    *
    * @param sort 排序
    * @param wrapper 自定义sql
    * @return DO
    */
    List<SysDataPermissionRuleRoleDO> queryAll(SysDataPermissionRuleRoleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
}