package com.boot.admin.system.modules.mnt.mapstruct;

import com.boot.admin.system.modules.mnt.domain.DatabaseDO;
import com.boot.admin.system.modules.mnt.dto.DatabaseDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>DatabaseMapStruct interface.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DatabaseMapStruct extends BaseMapStruct<DatabaseDO, DatabaseDTO, DatabaseDTO> {

}
