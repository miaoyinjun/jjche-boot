package com.boot.admin.log.modules.logging.mapstruct;

import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.modules.logging.vo.LogVO;
import com.boot.admin.core.base.BaseMapStruct;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>LogMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2019-5-22
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogMapStruct extends BaseMapStruct<LogDO, LogVO, LogVO> {

}
