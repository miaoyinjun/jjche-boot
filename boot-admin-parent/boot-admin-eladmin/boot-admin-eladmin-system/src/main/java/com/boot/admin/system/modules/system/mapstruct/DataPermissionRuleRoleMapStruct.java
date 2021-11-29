package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleRoleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionRuleRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据规则权限 转换类
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 * @version 1.0.1-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionRuleRoleMapStruct extends BaseMapStruct<DataPermissionRuleRoleDO, DataPermissionRuleRoleDTO, DataPermissionRuleRoleVO> {
}
