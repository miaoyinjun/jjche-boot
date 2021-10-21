package com.boot.admin.demo.modules.student.function;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.boot.admin.demo.modules.student.service.StudentService;
import com.boot.admin.log.biz.service.IParseFunction;
import com.boot.admin.demo.modules.student.domain.StudentDO;
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
 * @since 2021-04-29
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class StudentNameByIdsParseFunction implements IParseFunction<Set> {
    @Resource
    @Lazy
    private StudentService studentService;

    /** {@inheritDoc} */
    @Override
    public String functionName() {
        return "STUDENT_NAME_BY_IDS";
    }

    /** {@inheritDoc} */
    @Override
    public String apply(Set value) {
        QueryWrapper<StudentDO> studentQueryWrapper = Wrappers.query();
        studentQueryWrapper.select("name");
        studentQueryWrapper.in("id", Convert.convert(List.class, value));
        List<Object> list = studentService.getBaseMapper().selectObjs(studentQueryWrapper);
        if (CollUtil.isNotEmpty(list)) {
            return CollUtil.join(list, StrUtil.COMMA);
        }
        return null;
    }
}
