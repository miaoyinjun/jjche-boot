package org.jjche.system.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.security.dto.JobSmallDto;
import org.jjche.system.modules.system.domain.JobDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>JobSmallMapper interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobSmallMapper extends BaseMapStruct<JobDO, JobSmallDto, JobSmallDto> {

}
