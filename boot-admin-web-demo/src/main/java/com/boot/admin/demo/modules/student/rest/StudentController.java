package com.boot.admin.demo.modules.student.rest;

import com.boot.admin.common.dto.BaseDTO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.ApiRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.demo.constant.ApiVersion;
import com.boot.admin.demo.modules.student.api.dto.StudentDTO;
import com.boot.admin.demo.modules.student.api.dto.StudentQueryCriteriaDTO;
import com.boot.admin.demo.modules.student.api.enums.CourseEnum;
import com.boot.admin.demo.modules.student.api.enums.StudentSortEnum;
import com.boot.admin.demo.modules.student.api.vo.StudentVO;
import com.boot.admin.demo.modules.student.service.StudentService;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "学生")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("students")
@RequiredArgsConstructor
public class StudentController extends BaseController {

    private final StudentService studentService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link com.boot.admin.demo.modules.student.api.dto.StudentDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PostMapping
    @ApiOperation(value = "学生-新增", tags = ApiVersion.VERSION_1_0_0)
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('student:add')")
    @LogRecordAnnotation(
            value = "创建了一个学生, 学生姓名：「{{#dto.name}}」",
            category = LogCategoryType.OPERATING,
            type = LogType.ADD, module = ApiVersion.MODULE_STUDENT,
            prefix = "student", bizNo = "{{#dto.name}}"
    )
    public ResultWrapper create(@Validated @Valid @RequestBody StudentDTO dto) {
        studentService.save(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @DeleteMapping
    @ApiOperation(value = "学生-删除", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:del')")
    @LogRecordAnnotation(
            value = "被删除的学生姓名是...", category = LogCategoryType.OPERATING,
            type = LogType.DELETE, module = ApiVersion.MODULE_STUDENT,
            prefix = "student", bizNo = "{{#ids}}",
            detail = "学生姓名：「{STUDENT_NAME_BY_IDS{#ids}}」"
    )
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        studentService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param dto a {@link com.boot.admin.demo.modules.student.api.dto.StudentDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PutMapping
    @ApiOperation(value = "学生-修改", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:edit')")
    @LogRecordAnnotation(
            value = "被修改的学生姓名：「{{#dto.name}}」", category = LogCategoryType.OPERATING,
            type = LogType.UPDATE, module = ApiVersion.MODULE_STUDENT,
            prefix = "student", bizNo = "{{#dto.name}}",
            detail = "修改内容：「{STUDENT_UPDATE_DIFF_BY_DTO{#dto}}」"
    )
    public ResultWrapper update(@Validated(BaseDTO.Update.class) @Valid @RequestBody StudentDTO dto) {
        studentService.update(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "学生-查询单个", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    @LogRecordAnnotation(
            value = "查询单个", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = ApiVersion.MODULE_STUDENT
    )
    public ResultWrapper<StudentVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.studentService.getVoById(id));
    }

    /**
     * <p>download.</p>
     *
     * @param sort     a {@link com.boot.admin.demo.modules.student.api.enums.StudentSortEnum} object.
     * @param criteria a {@link com.boot.admin.demo.modules.student.api.dto.StudentQueryCriteriaDTO} object.
     */
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "学生-导出", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = ApiVersion.MODULE_STUDENT
    )
    public void download(@ApiParam(value = "排序", required = true)
                         @NotNull(message = "排序字段不正确")
                         @RequestParam StudentSortEnum sort,
                         StudentQueryCriteriaDTO criteria) {
        studentService.download(sort, criteria);
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page  a {@link com.boot.admin.mybatis.param.PageParam} object.
     * @param sort  a {@link com.boot.admin.demo.modules.student.api.enums.StudentSortEnum} object.
     * @param query a {@link com.boot.admin.demo.modules.student.api.dto.StudentQueryCriteriaDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @param course a {@link com.boot.admin.demo.modules.student.api.enums.CourseEnum} object.
     */
    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = ApiVersion.MODULE_STUDENT
    )
    public ResultWrapper<MyPage<StudentVO>> pageQuery(PageParam page,
                                                      @ApiParam(value = "排序", required = true)
                                                      @NotNull(message = "排序字段不正确")
                                                      @RequestParam StudentSortEnum sort,
                                                      @ApiParam(value = "课程")
                                                      @RequestParam(required = false) CourseEnum course,
                                                      @Validated StudentQueryCriteriaDTO query) {
        return ResultWrapper.ok(studentService.pageQuery(page, sort, course, query));
    }

}
