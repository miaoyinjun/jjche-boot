package org.jjche.demo.modules.a.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.demo.modules.a.domain.TeacherDO;
import org.jjche.demo.modules.a.api.vo.TeacherVO;
import org.jjche.demo.modules.a.api.dto.TeacherDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* <p>
* ss 转换类
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapStruct extends BaseMapStruct<TeacherDO, TeacherDTO, TeacherVO> {
}