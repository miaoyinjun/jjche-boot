package org.jjche.system.modules.system.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.system.modules.system.api.dto.MenuDTO;
import org.jjche.system.modules.system.domain.MenuDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>MenuMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-17
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapStruct extends BaseMapStruct<MenuDO, MenuDTO, MenuDTO> {
}
