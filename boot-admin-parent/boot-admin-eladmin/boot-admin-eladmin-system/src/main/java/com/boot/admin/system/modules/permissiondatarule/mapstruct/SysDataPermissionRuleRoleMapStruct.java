package com.boot.admin.system.modules.permissiondatarule.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleRoleDTO;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleRoleVO;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
* 数据规则权限 转换类
* </p>
*
* @author miaoyj
* @since 2021-11-01
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDataPermissionRuleRoleMapStruct extends BaseMapStruct<SysDataPermissionRuleRoleDO, SysDataPermissionRuleRoleDTO, SysDataPermissionRuleRoleVO> {
}