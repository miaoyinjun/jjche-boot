package com.boot.admin.demo.modules.student.function;

import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.demo.modules.student.api.dto.StudentDTO;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.demo.modules.student.domain.StudentDO;
import com.boot.admin.log.biz.service.IParseFunction;
import com.boot.admin.demo.modules.student.service.StudentService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 组装字段修改前后值
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-29
 */
@Component
public class StudentUpdateDiffByDtoParseFunction implements IParseFunction<StudentDTO> {
    @Resource
    private StudentService studentService;

    /** {@inheritDoc} */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String functionName() {
        return "STUDENT_UPDATE_DIFF_BY_DTO";
    }

    /** {@inheritDoc} */
    @Override
    public String apply(StudentDTO value) {
        if (ObjectUtil.isNotNull(value)) {
            StudentDO studentDO = studentService.getById(value.getId());
            return StrUtil.updateDiffByDoDto(studentDO, value);
        }
        return null;
    }
}
