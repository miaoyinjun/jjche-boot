package com.boot.admin.system.modules.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 角色菜单数据字段权限关联
 * </p>
 *
 * @author miaoyj
 * @since 2020-12-03
 * @version 1.0.10-SNAPSHOT
 */
@Data
@NoArgsConstructor
public class RoleMenuDataPermissionFieldDTO {
    /** 菜单id*/
    private Long menuId;
    /** 数据字段权限id*/
    private List<Long> dataPermissionFieldIds;
}
