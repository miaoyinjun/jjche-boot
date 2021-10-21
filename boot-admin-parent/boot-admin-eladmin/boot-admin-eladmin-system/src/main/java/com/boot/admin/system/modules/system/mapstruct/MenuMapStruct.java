package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.MenuDO;
import com.boot.admin.system.modules.system.dto.MenuDTO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>MenuMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2018-12-17
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapStruct extends BaseMapStruct<MenuDO, MenuDTO, MenuDTO> {
}
