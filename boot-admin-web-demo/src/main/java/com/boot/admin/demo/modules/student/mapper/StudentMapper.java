package com.boot.admin.demo.modules.student.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.demo.modules.student.api.enums.StudentSortEnum;
import com.boot.admin.demo.modules.student.api.vo.StudentVO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.demo.modules.student.domain.StudentDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
public interface StudentMapper extends MyBaseMapper<StudentDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page 分页
     * @param sort 排序
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<StudentVO> pageQuery(PageParam page, StudentSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param sort 排序
     * @param wrapper 自定义sql
     * @return DO
     */
    List<StudentDO> queryAll(StudentSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
}
