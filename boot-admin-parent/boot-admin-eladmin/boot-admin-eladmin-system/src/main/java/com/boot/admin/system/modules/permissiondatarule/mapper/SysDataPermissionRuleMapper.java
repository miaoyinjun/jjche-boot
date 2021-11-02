package com.boot.admin.system.modules.permissiondatarule.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.permissiondatarule.api.enums.SysDataPermissionRuleSortEnum;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleVO;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据规则
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-27
 */
public interface SysDataPermissionRuleMapper extends MyBaseMapper<SysDataPermissionRuleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param sort    排序
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<SysDataPermissionRuleVO> pageQuery(PageParam page, SysDataPermissionRuleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param sort    排序
     * @param wrapper 自定义sql
     * @return DO
     */
    List<SysDataPermissionRuleDO> queryAll(SysDataPermissionRuleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 根据角色id和权限标识查询
     * </p>
     *
     * @param roleIds    角色id
     * @param permission 权限标识
     * @return /
     */
    List<PermissionDataRuleDTO> selectByRoleIdsAndPermission(@Param("roleIds")
                                                                     Set<Long> roleIds,
                                                             @Param("permission")
                                                                     String permission);

    /**
     * <p>
     * 根据角色id和权限标识查询
     * </p>
     *
     * @param menuId 菜单id
     * @param roleId 角色id
     * @return /
     */
    MyPage<SysDataPermissionRuleVO> pageByMenuIdAndRoleId(PageParam page,
                                                            @Param("menuId") Long menuId,
                                                            @Param("roleId") Long roleId);

}