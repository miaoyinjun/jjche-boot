package com.boot.admin.system.modules.permissiondatarule.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleRoleDTO;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleRoleDO;
import com.boot.admin.system.modules.permissiondatarule.mapper.SysDataPermissionRuleRoleMapper;
import com.boot.admin.system.modules.permissiondatarule.mapstruct.SysDataPermissionRuleRoleMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据规则权限 服务实现类
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 */
@Service
@RequiredArgsConstructor
public class SysDataPermissionRuleRoleService extends MyServiceImpl<SysDataPermissionRuleRoleMapper, SysDataPermissionRuleRoleDO> {

    private final SysDataPermissionRuleRoleMapStruct sysDataPermissionRuleRoleMapStruct;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDataPermissionRuleRoleDTO dto) {
        Long roleId = dto.getRoleId();
        Long menuId = dto.getMenuId();
        //删除
        LambdaQueryWrapper<SysDataPermissionRuleRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(SysDataPermissionRuleRoleDO::getRoleId, roleId);
        queryWrapper.in(SysDataPermissionRuleRoleDO::getMenuId, menuId);
        this.remove(queryWrapper);

        List<Long> dataPermissionRuleIdList = dto.getDataPermissionRuleIdList();
        if (CollUtil.isNotEmpty(dataPermissionRuleIdList)) {
            List<SysDataPermissionRuleRoleDO> sysDataPermissionRuleRoleDOList = CollUtil.newArrayList();
            for (Long id : dataPermissionRuleIdList) {
                SysDataPermissionRuleRoleDO sysDataPermissionRuleRoleDO = new SysDataPermissionRuleRoleDO();
                sysDataPermissionRuleRoleDO.setRoleId(roleId);
                sysDataPermissionRuleRoleDO.setMenuId(menuId);
                sysDataPermissionRuleRoleDO.setDataPermissionRuleId(id);
                sysDataPermissionRuleRoleDOList.add(sysDataPermissionRuleRoleDO);
            }
            Assert.isTrue(this.saveBatch(sysDataPermissionRuleRoleDOList), "保存失败");
        }
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param permissionRuleIds 数据规则id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPermissionRuleIds(Set<Long> permissionRuleIds) {
        LambdaQueryWrapper<SysDataPermissionRuleRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(SysDataPermissionRuleRoleDO::getDataPermissionRuleId, permissionRuleIds);
        this.remove(queryWrapper);
    }
}