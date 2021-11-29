package com.boot.admin.system.modules.system.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>
 * 角色数据字段关联
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
    /** 数据字段id*/
    private List<Long> dataPermissionFieldIds;
}
