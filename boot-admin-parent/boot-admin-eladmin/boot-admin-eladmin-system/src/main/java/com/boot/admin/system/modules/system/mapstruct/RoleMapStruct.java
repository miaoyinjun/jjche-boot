package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.RoleDO;
import com.boot.admin.system.modules.system.dto.RoleDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>RoleMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2018-11-23
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapStruct extends BaseMapStruct<RoleDO, RoleDTO, RoleDTO> {

}
