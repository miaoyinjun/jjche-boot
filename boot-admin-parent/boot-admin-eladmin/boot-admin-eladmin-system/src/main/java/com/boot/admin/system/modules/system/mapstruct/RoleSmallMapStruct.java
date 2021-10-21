package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.RoleDO;
import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.security.dto.RoleSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>RoleSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-5-23
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleSmallMapStruct extends BaseMapStruct<RoleDO, RoleSmallDto, RoleSmallDto> {

}
