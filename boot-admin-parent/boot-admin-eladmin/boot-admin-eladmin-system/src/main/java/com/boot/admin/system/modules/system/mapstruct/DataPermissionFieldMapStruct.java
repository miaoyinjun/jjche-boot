package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.DataPermissionFieldDO;
import com.boot.admin.system.modules.system.dto.DataPermissionFieldDTO;
import com.boot.admin.system.modules.system.vo.DataPermissionFieldVO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 数据权限列映射
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-18
 * @version 1.0.10-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionFieldMapStruct extends BaseMapStruct<DataPermissionFieldDO, DataPermissionFieldDTO, DataPermissionFieldVO> {

}
