package com.boot.admin.system.modules.mnt.mapstruct;

import com.boot.admin.system.modules.mnt.domain.DeployDO;
import com.boot.admin.system.modules.mnt.dto.DeployDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DeployMapStruct interface.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", uses = {AppMapStruct.class, ServerDeployMapStruct.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeployMapStruct extends BaseMapStruct<DeployDO, DeployDTO, DeployDTO> {

}
