package com.boot.admin.tool.modules.tool.mapstruct;

import com.boot.admin.tool.modules.tool.domain.LocalStorageDO;
import com.boot.admin.tool.modules.tool.dto.LocalStorageDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>LocalStorageMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-09-05
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LocalStorageMapStruct extends BaseMapStruct<LocalStorageDO, LocalStorageDTO, LocalStorageDTO> {

}
