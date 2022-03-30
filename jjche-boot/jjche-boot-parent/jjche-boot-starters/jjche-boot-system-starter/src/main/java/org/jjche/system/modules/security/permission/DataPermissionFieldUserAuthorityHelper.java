package org.jjche.system.modules.security.permission;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.permission.IDataPermissionFieldUserAuthorityHelper;
import org.jjche.common.util.ClassCompareUtil;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.core.util.SecurityUtils;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldVO;
import org.jjche.system.modules.system.service.DataPermissionFieldRoleService;
import org.jjche.system.modules.system.service.DataPermissionFieldService;
import org.jjche.system.modules.system.service.MenuService;
import org.jjche.system.modules.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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
    public List<DataPermissionFieldResultVO> getDataResource(String permission, Class returnClass, boolean isFilter) {
        List<DataPermissionFieldResultVO> resources = new ArrayList<>();
        if (returnClass != null) {
            Map<String, String> map = ClassCompareUtil.getApiModelPropertyValue(returnClass);
            //获取list里的类型，取ApiModelProperty注解里的字段名，相同的字段名优先配置里的名称
            for (String key : map.keySet()) {
                String value = MapUtil.getStr(map, key);
                DataPermissionFieldResultVO dataPermissionFieldResultVO = DataPermissionFieldResultVO.builder()
                        .name(value)
                        .code(key)
                        .isAccessible(true)
                        .isEditable(true)
                        .build();
                resources.add(dataPermissionFieldResultVO);
            }
        }
        if (isFilter) {
            //获取用户数据规则配置
            if (StrUtil.isNotBlank(permission)) {
                List<DataPermissionFieldVO> permissionDataFieldDTOList =
                        dataPermissionFieldService.listByUserId(SecurityUtils.getCurrentUserId());
                if (CollUtil.isNotEmpty(permissionDataFieldDTOList)) {
                    String finalPermissionCode = permission;
                    Predicate condition = (str) -> StrUtil.equals(String.valueOf(str), finalPermissionCode);
                    permissionDataFieldDTOList = permissionDataFieldDTOList.stream().filter((p) -> (condition.test(p.getMenuPermission()))).collect(Collectors.toList());
                    if (CollUtil.isNotEmpty(permissionDataFieldDTOList)) {
                        for (DataPermissionFieldResultVO resource : resources) {
                            for (DataPermissionFieldVO permissionFieldVO : permissionDataFieldDTOList) {
                                if (StrUtil.equals(permissionFieldVO.getCode(), resource.getCode())) {
                                    resource.setIsAccessible(permissionFieldVO.getIsAccessible());
                                    resource.setIsEditable(permissionFieldVO.getIsEditable());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return resources;
    }
}
