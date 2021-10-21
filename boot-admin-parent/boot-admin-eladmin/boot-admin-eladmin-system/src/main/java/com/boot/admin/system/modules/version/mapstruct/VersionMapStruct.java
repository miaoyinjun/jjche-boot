package com.boot.admin.system.modules.version.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.version.domain.VersionDO;
import com.boot.admin.system.modules.version.vo.VersionVO;
import com.boot.admin.system.modules.version.dto.VersionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 版本 转换类
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-23
 * @version 1.0.0-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VersionMapStruct extends BaseMapStruct<VersionDO, VersionDTO, VersionVO> {
}
