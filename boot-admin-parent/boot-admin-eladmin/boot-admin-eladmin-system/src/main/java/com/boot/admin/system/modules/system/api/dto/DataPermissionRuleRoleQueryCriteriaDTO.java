package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据规则权限 查询
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 */
@Data
public class DataPermissionRuleRoleQueryCriteriaDTO extends BaseQueryCriteriaDTO {
    /**
     * 精确
     */
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    @QueryCriteria(propName = "menu_id")
    private Long menuId;
    /**
     * 精确
     */
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID", required = true)
    @QueryCriteria(propName = "role_id")
    private Long roleId;

}