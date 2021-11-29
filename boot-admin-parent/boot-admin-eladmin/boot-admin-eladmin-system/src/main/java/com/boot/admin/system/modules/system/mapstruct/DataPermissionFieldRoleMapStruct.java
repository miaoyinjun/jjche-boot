package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldRoleDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionFieldRoleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldRoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据字段角色 转换类
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-04
 * @version 1.0.1-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionFieldRoleMapStruct extends BaseMapStruct<DataPermissionFieldRoleDO, DataPermissionFieldRoleDTO, DataPermissionFieldRoleVO> {
}
