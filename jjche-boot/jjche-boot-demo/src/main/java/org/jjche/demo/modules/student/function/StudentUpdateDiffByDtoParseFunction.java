package org.jjche.demo.modules.student.function;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.mapstruct.StudentMapStruct;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.context.LogRecordContext;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.springframework.stereotype.Component;

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
@RequiredArgsConstructor
public class StudentUpdateDiffByDtoParseFunction implements IParseFunction<Long> {
    private final StudentService studentService;
    private final StudentMapStruct studentMapStruct;

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
    public String apply(Long id) {
        StudentDO studentDO = studentService.getById(id);
        Assert.notNull(studentDO, "记录不存在");
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, this.studentMapStruct.toDTO(studentDO));
        return null;
    }
}