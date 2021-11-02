package com.boot.admin.system.modules.permissiondatarule.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleDO;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleVO;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
* 数据规则 转换类
* </p>
*
* @author miaoyj
* @since 2021-10-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SysDataPermissionRuleMapStruct extends BaseMapStruct<SysDataPermissionRuleDO, SysDataPermissionRuleDTO, SysDataPermissionRuleVO> {
}