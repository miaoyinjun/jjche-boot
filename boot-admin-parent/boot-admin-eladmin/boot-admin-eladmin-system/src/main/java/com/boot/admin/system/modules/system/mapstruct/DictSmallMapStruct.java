package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.DictDO;
import com.boot.admin.system.modules.system.api.dto.DictSmallDto;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DictSmallMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-04-10
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictSmallMapStruct extends BaseMapStruct<DictDO, DictSmallDto, DictSmallDto> {

}
