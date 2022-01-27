package org.jjche.demo.modules.student.mapstruct;

import org.jjche.core.base.BaseMapStruct;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.api.vo.StudentVO;
import org.jjche.demo.modules.student.domain.StudentDO;
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
public interface StudentMapStruct extends BaseMapStruct<StudentDO, StudentDTO, StudentVO> {
}
