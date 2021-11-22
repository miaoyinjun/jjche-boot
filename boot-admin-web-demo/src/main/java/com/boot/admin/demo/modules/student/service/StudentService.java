package com.boot.admin.demo.modules.student.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.annotation.PermissionData;
import com.boot.admin.common.dto.IdQueryCriteriaDTO;
import com.boot.admin.common.dto.IdsQueryCriteriaDTO;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.demo.modules.student.api.dto.StudentDTO;
import com.boot.admin.demo.modules.student.api.dto.StudentQueryCriteriaDTO;
import com.boot.admin.demo.modules.student.api.enums.CourseEnum;
import com.boot.admin.demo.modules.student.api.enums.StudentSortEnum;
import com.boot.admin.demo.modules.student.api.vo.StudentDetailVO;
import com.boot.admin.demo.modules.student.api.vo.StudentVO;
import com.boot.admin.demo.modules.student.domain.StudentDO;
import com.boot.admin.demo.modules.student.mapper.StudentMapper;
import com.boot.admin.demo.modules.student.mapstruct.StudentDetailMapStruct;
import com.boot.admin.demo.modules.student.mapstruct.StudentMapStruct;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.security.permission.field.DataPermissionFieldResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    private final StudentDetailMapStruct studentDetailMapStruct;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(StudentDTO dto) {
        StudentDO studentDO = this.studentMapStruct.toDO(dto);
        Assert.isTrue(this.save(studentDO), "保存失败");
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param query 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(IdsQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        Assert.isTrue(this.removeBatchWithFill(new StudentDO(), queryWrapper) == query.getIds().size(), "删除失败，记录不存在或权限不足");
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param query 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(StudentDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        StudentDO studentDO = this.getOne(queryWrapper);
        Assert.notNull(studentDO, "记录不存在");
        studentDO = this.studentMapStruct.toDO(query);
        Assert.isTrue(this.updateById(studentDO), "修改失败，记录不存在或权限不足");
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param query 条件
     * @return StudentVO 对象
     */
    public DataPermissionFieldResult<StudentDetailVO> getVoById(IdQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        StudentDO studentDO = this.getOne(queryWrapper);
        Assert.notNull(studentDO, "记录不存在或权限不足");
        return DataPermissionFieldResult.build(studentDetailMapStruct.toVO(studentDO));
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query  条件
     * @param sort   排序
     * @param page   分页
     * @param course a {@link com.boot.admin.demo.modules.student.api.enums.CourseEnum} object.
     * @return StudentVO 分页
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
    @PermissionData(fieldReturn = true)
    public DataPermissionFieldResult<StudentVO> listQueryAll(StudentSortEnum sort, StudentQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        List<StudentDO> list = this.baseMapper.queryAll(sort, queryWrapper);
        return DataPermissionFieldResult.build(studentMapStruct.toVO(list));
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @param dataPermissionFieldResult 结果
     */
    public void download(DataPermissionFieldResult<StudentVO> dataPermissionFieldResult) {
        List<Map<String, Object>> list = DataPermissionFieldResult.toExcelListMap(dataPermissionFieldResult);
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw new IllegalArgumentException("文件下载失败");
        }
    }
}
