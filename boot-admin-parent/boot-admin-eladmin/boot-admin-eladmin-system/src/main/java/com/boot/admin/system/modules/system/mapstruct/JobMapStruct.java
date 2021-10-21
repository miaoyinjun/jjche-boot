package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.JobDO;
import com.boot.admin.system.modules.system.dto.JobDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>JobMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-03-29
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapStruct extends BaseMapStruct<JobDO, JobDTO, JobDTO> {
}
