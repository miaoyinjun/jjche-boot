package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 数据字段角色
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-04
 */
@Data
public class DataPermissionFieldRoleDTO implements BaseDTO {
    @NotNull(message = "id不能为空", groups = Update.class)
    @ApiModelProperty(value = "ID")
    private Long id;
    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID")
    private Long menuId;
    @ApiModelProperty(value = "数据字段权限")
    List<DataPermissionFiledRoleSelectedDTO> dataPermissionFieldSelectedList;
}