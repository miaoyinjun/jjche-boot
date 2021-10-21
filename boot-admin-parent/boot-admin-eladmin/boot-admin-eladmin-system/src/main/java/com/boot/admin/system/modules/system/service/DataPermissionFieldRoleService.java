package com.boot.admin.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldRoleDO;
import com.boot.admin.system.modules.system.dto.RoleMenuDataPermissionFieldDTO;
import com.boot.admin.system.modules.system.mapper.DataPermissionFieldRoleMapper;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据字段权限角色服务
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class DataPermissionFieldRoleService extends MyServiceImpl<DataPermissionFieldRoleMapper, DataPermissionFieldRoleDO> {

    /**
     * <p>
     * 批量删除
     * </p>
     *
     * @param roleId 角色id
     */
    public void deleteInBatch(Long roleId) {
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataPermissionFieldRoleDO::getRoleId, roleId);
        this.remove(queryWrapper);
    }

    /**
     * <p>
     * 批量保存
     * </p>
     *
     * @param list 资源
     */
    public void saveAll(List<DataPermissionFieldRoleDO> list) {
        this.saveBatch(list);
    }

    /**
     * <p>
     * 删除原数据，并保存新数据
     * </p>
     *
     * @param roleId                       角色id
     * @param roleMenuDataPermissionFields 数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAndSaveAll(Long roleId,
                                 List<RoleMenuDataPermissionFieldDTO> roleMenuDataPermissionFields) {
        if (CollUtil.isNotEmpty(roleMenuDataPermissionFields)) {
            List<DataPermissionFieldRoleDO> dataPermissionFieldRoleList = CollUtil.newArrayList();
            for (RoleMenuDataPermissionFieldDTO field : roleMenuDataPermissionFields) {
                Long menuId = field.getMenuId();
                List<Long> dataPermissionFieldIds = field.getDataPermissionFieldIds();
                if (CollUtil.isNotEmpty(dataPermissionFieldIds)) {
                    for (Long dataPermissionFieldId : dataPermissionFieldIds) {
                        DataPermissionFieldRoleDO dataPermissionFieldRole = new DataPermissionFieldRoleDO();
                        dataPermissionFieldRole.setRoleId(roleId);
                        dataPermissionFieldRole.setMenuId(menuId);
                        dataPermissionFieldRole.setDataPermissionFieldId(dataPermissionFieldId);
                        dataPermissionFieldRoleList.add(dataPermissionFieldRole);
                    }
                }
            }
            //删除该角色所有菜单数据字段权限
            this.deleteInBatch(roleId);
            this.saveAll(dataPermissionFieldRoleList);
        }
    }

    /**
     * <p>
     * 根据菜单与角色id查询
     * </p>
     *
     * @param menuId  角色id
     * @param roleIds 菜单id
     * @return /
     */
    public List<DataPermissionFieldRoleDO> findByMenuIdAndRoleIdIn(Long menuId, List<Long> roleIds) {
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataPermissionFieldRoleDO::getMenuId, menuId);
        queryWrapper.in(DataPermissionFieldRoleDO::getRoleId, roleIds);
        return this.list(queryWrapper);
    }
}
