package org.jjche.demo.modules.student.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jjche.demo.modules.student.domain.StudentDO;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.service.IParseFunction;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 根据ids查找学生姓名
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-29
 */
@Component
public class StudentNameByIdsParseFunction implements IParseFunction<Set> {
    @Resource
    @Lazy
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
        return "STUDENT_NAME_BY_IDS";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String apply(Set value) {
        LambdaQueryWrapper<StudentDO> studentQueryWrapper = Wrappers.lambdaQuery();
        studentQueryWrapper.select(StudentDO::getName);
        studentQueryWrapper.in(StudentDO::getId, value);
        List<Object> list = studentService.getBaseMapper().selectObjs(studentQueryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return CollUtil.join(list, StrUtil.COMMA);
        }
        return null;
    }
}
