package org.jjche.demo.modules.a.function;

import cn.hutool.core.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.jjche.demo.modules.a.domain.TeacherDO;
import org.jjche.demo.modules.a.mapstruct.TeacherMapStruct;
import org.jjche.demo.modules.a.service.TeacherService;
import org.jjche.log.biz.context.LogRecordContext;
import org.jjche.log.biz.service.IParseFunction;
import org.jjche.log.biz.service.impl.DiffParseFunction;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * <p>
 * ss 设置修改/删除前的数据到变量
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-19
 */
@Component
@RequiredArgsConstructor
public class TeacherDiffOldByIdParseFunction implements IParseFunction<Object> {
    private final TeacherService teacherService;
    private final TeacherMapStruct teacherMapStruct;

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
        return "TEACHER_DIFF_OLD_BY_ID";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Object idObj) {
        Object result = null;
        if (idObj instanceof List) {
            List<Long> ids = (List<Long>) idObj;
            List<TeacherDO> list = this.teacherService.listByIds(ids);
            result = this.teacherMapStruct.toDTO(list);
        } else {
            Long id = (Long) idObj;
            TeacherDO teacherDO = this.teacherService.getById(id);
            result = this.teacherMapStruct.toDTO(teacherDO);
        }
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, result);
        return null;
    }
}