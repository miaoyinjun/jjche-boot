package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionRuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
* 数据规则 转换类
* </p>
*
* @author miaoyj
* @since 2021-10-27
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataPermissionRuleMapStruct extends BaseMapStruct<DataPermissionRuleDO, DataPermissionRuleDTO, DataPermissionRuleVO> {
}