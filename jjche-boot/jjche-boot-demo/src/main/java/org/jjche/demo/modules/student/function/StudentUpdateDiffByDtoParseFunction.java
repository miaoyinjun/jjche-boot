package org.jjche.demo.modules.student.function;

import cn.hutool.core.util.ObjectUtil;
import org.jjche.common.util.StrUtil;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.service.IParseFunction;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executeBefore() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String functionName() {
        return "STUDENT_UPDATE_DIFF_BY_DTO";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(StudentDTO value) {
        if (ObjectUtil.isNotNull(value)) {
            StudentDO studentDO = studentService.getById(value.getId());
            return StrUtil.updateDiffByDoDto(studentDO, value);
        }
        return null;
    }
}
