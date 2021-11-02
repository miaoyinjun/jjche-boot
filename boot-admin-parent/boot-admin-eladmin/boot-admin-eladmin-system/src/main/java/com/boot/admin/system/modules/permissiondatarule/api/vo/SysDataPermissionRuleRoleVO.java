package com.boot.admin.system.modules.permissiondatarule.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
/**
* <p>
* 数据规则权限
* </p>
*
* @author miaoyj
* @since 2021-11-01
*/
@Data
public class SysDataPermissionRuleRoleVO implements Serializable {
   @ApiModelProperty(value = "ID")
   private Long id;
   @ApiModelProperty(value = "角色ID")
   private Long roleId;
   @ApiModelProperty(value = "菜单ID")
   private Long menuId;
   @ApiModelProperty(value = "数据规则权限ID")
   private Long dataPermissionRuleId;
}