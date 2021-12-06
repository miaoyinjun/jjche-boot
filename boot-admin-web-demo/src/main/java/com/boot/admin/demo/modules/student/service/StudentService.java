package com.boot.admin.demo.modules.student.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.demo.modules.student.api.dto.StudentDTO;
import com.boot.admin.demo.modules.student.api.dto.StudentQueryCriteriaDTO;
import com.boot.admin.demo.modules.student.api.enums.CourseEnum;
import com.boot.admin.demo.modules.student.api.enums.StudentSortEnum;
import com.boot.admin.demo.modules.student.api.vo.StudentVO;
import com.boot.admin.demo.modules.student.domain.StudentDO;
import com.boot.admin.demo.modules.student.mapper.StudentMapper;
import com.boot.admin.demo.modules.student.mapstruct.StudentMapStruct;
import com.boot.admin.log.biz.context.LogRecordContext;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 学生 服务实现类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Service
@RequiredArgsConstructor
public class StudentService extends MyServiceImpl<StudentMapper, StudentDO> {

    private final StudentMapStruct studentMapStruct;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     * @return a {@link java.lang.Long} object.
     */
    @Transactional(rollbackFor = Exception.class)
    public Long save(StudentDTO dto) {
        StudentDO studentDO = this.studentMapStruct.toDO(dto);
        Assert.isTrue(this.save(studentDO), "保存失败");
        return studentDO.getId();
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        LogRecordContext.putVariable("test", "abcc");
        Assert.isTrue(this.removeBatchByIdsWithFill(new StudentDO(), ids) == ids.size(), "删除失败，记录不存在");
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentDTO dto) {
        StudentDO studentDO = this.getById(dto.getId());
        Assert.notNull(studentDO, "记录不存在");
        studentDO = this.studentMapStruct.toDO(dto);
        Assert.isTrue(this.updateById(studentDO), "修改失败，记录不存在");
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return StudentVO 对象
     */
    public StudentVO getVoById(Long id) {
        StudentDO studentDO = this.getById(id);
        Assert.notNull(studentDO, "记录不存在");
        return this.studentMapStruct.toVO(studentDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query 条件
     * @param sort  排序
     * @param page  分页
     * @return StudentVO 分页
     * @param course a {@link com.boot.admin.demo.modules.student.api.enums.CourseEnum} object.
     */
    public MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, CourseEnum course, StudentQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        if (course != null) {
            queryWrapper.eq("course", course);
        }
        return this.baseMapper.pageQuery(page, sort, queryWrapper);
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param sort  排序
     * @param query 条件
     * @return StudentVO 列表对象
     */
    public List<StudentVO> listQueryAll(StudentSortEnum sort, StudentQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        List<StudentDO> list = this.baseMapper.queryAll(sort, queryWrapper);
        return studentMapStruct.toVO(list);
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @param sort  排序
     * @param query 条件
     */
    public void download(StudentSortEnum sort, StudentQueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<StudentVO> all = this.listQueryAll(sort, query);
        for (StudentVO student : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("姓名", student.getName());
            map.put("年龄", student.getAge());
            map.put("课程", student.getCourse().getDesc());
            map.put("创建时间", student.getGmtCreate());
            map.put("创建者", student.getCreatedBy());
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw new IllegalArgumentException("文件下载失败");
        }
    }
}
