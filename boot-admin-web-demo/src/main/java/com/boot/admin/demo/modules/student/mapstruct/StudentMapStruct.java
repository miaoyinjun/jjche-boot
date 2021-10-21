package com.boot.admin.demo.modules.student.mapstruct;

import com.boot.admin.demo.modules.student.api.dto.StudentDTO;
import com.boot.admin.demo.modules.student.api.vo.StudentVO;
import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.demo.modules.student.domain.StudentDO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * 学生 转换类
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapStruct extends BaseMapStruct<StudentDO, StudentDTO, StudentVO> {
}
