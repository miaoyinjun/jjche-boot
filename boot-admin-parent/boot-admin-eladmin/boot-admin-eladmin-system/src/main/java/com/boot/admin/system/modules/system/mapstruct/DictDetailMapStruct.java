package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.DictDetailDO;
import com.boot.admin.system.modules.system.dto.DictDetailDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DictDetailMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-04-10
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", uses = {DictSmallMapStruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DictDetailMapStruct extends BaseMapStruct<DictDetailDO, DictDetailDTO, DictDetailDTO> {

}
