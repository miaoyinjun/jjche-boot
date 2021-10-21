package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.DeptDO;
import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.security.dto.DeptSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DeptSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-03-25
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeptSmallMapStruct extends BaseMapStruct<DeptDO, DeptSmallDto, DeptSmallDto> {

}
