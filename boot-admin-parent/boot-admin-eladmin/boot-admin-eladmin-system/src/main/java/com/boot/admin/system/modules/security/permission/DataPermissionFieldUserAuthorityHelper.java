package com.boot.admin.system.modules.security.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.core.permission.DataPermissionFieldResultVO;
import com.boot.admin.core.permission.IDataPermissionFieldUserAuthorityHelper;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.security.dto.RoleSmallDto;
import com.boot.admin.security.dto.UserVO;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldDO;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldRoleDO;
import com.boot.admin.system.modules.system.domain.MenuDO;
import com.boot.admin.system.modules.system.service.DataPermissionFieldRoleService;
import com.boot.admin.system.modules.system.service.DataPermissionFieldService;
import com.boot.admin.system.modules.system.service.MenuService;
import com.boot.admin.system.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户权限工具类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
@Service
public class DataPermissionFieldUserAuthorityHelper implements IDataPermissionFieldUserAuthorityHelper {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;
    @Autowired
    private DataPermissionFieldRoleService dataPermissionFieldRoleService;
    @Autowired
    private DataPermissionFieldService dataPermissionFieldService;
    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataPermissionFieldResultVO> getDataResource(String permission) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<DataPermissionFieldResultVO> resources = new ArrayList<>();
        MenuDO menu = menuService.findByPermission(permission);
        if (ObjectUtil.isNotNull(menu)) {
            Long menuId = menu.getId();
            List<DataPermissionFieldDO> dataPermissionFields = dataPermissionFieldService.findByMenuId(menuId);
            if (ObjectUtil.isNotNull(userId) && CollUtil.isNotEmpty(dataPermissionFields)) {
                UserVO userDto = userService.findById(userId);
                List<RoleSmallDto> roleList = userDto.getRoles();
                if (CollUtil.isNotEmpty(roleList)) {
                    List<Long> roleIdList = roleList.stream().map(RoleSmallDto::getId).collect(Collectors.toList());
                    List<DataPermissionFieldRoleDO> dataPermissionFieldRoleList = dataPermissionFieldRoleService.findByMenuIdAndRoleIdIn(menuId, roleIdList);
                    List<Long> dataPermissionFieldIdList = dataPermissionFieldRoleList.stream().map(DataPermissionFieldRoleDO::getDataPermissionFieldId).collect(Collectors.toList());
                    resources = Convert.toList(DataPermissionFieldResultVO.class, dataPermissionFields);
                    for (DataPermissionFieldResultVO resource : resources) {
                        Long dataPermissionFieldId = resource.getId();
                        Boolean isAccessible = !CollUtil.contains(dataPermissionFieldIdList, dataPermissionFieldId);
                        resource.setIsAccessible(isAccessible);
                    }
                }
            }
        }
        return resources;
    }
}
