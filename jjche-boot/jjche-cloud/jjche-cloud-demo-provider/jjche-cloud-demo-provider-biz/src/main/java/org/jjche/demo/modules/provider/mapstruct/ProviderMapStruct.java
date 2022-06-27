package org.jjche.demo.modules.provider.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.demo.modules.provider.api.dto.ProviderDTO;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.domain.ProviderDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 学生 转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProviderMapStruct extends BaseMapStruct<ProviderDO, ProviderDTO, ProviderVO> {
}
