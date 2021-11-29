package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 数据规则权限
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 * @version 1.0.1-SNAPSHOT
 */
@Data
public class DataPermissionRuleRoleDTO implements BaseDTO {
   @NotNull(message = "角色ID不能为空")
   @ApiModelProperty(value = "角色ID")
   private Long roleId;
   @NotNull(message = "菜单ID不能为空")
   @ApiModelProperty(value = "菜单ID")
   private Long menuId;
   @ApiModelProperty(value = "数据规则权限ID")
   private List<Long> dataPermissionRuleIdList;

}
