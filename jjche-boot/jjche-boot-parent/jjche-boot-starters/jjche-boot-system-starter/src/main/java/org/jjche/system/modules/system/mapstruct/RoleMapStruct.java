package org.jjche.system.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.system.api.dto.RoleDTO;
import org.jjche.system.modules.system.domain.RoleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>RoleMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapStruct extends BaseMapStruct<RoleDO, RoleDTO, RoleDTO> {

}
