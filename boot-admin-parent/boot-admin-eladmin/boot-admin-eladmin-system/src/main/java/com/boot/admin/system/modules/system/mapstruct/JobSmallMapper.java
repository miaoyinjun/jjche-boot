package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.JobDO;
import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.security.dto.JobSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>JobSmallMapper interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-03-29
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobSmallMapper extends BaseMapStruct<JobDO, JobSmallDto, JobSmallDto> {

}
