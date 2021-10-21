package com.boot.admin.log.modules.logging.mapstruct;

import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.modules.logging.dto.LogErrorDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>LogErrorMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-5-22
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogErrorMapStruct extends BaseMapStruct<LogDO, LogErrorDTO, LogErrorDTO> {

}
