package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.BaseDTO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.constant.ApiVersion;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.api.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.api.enums.CourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentVO;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * @param dto a {@link StudentDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @PostMapping
    @ApiOperation(value = "学生-新增", tags = ApiVersion.VERSION_1_0_0)
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('student:add')")
    @LogRecordAnnotation(
            value = "创建了一个学生, 学生姓名：「{{#dto.name}}」",
            category = LogCategoryType.OPERATING,
            type = LogType.ADD, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#_ret.data}}"
    )
    public ResultWrapper<Long> create(@Validated @Valid @RequestBody StudentDTO dto) {
        return ResultWrapper.ok(studentService.save(dto));
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @DeleteMapping
    @ApiOperation(value = "学生-删除", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:del')")
    @LogRecordAnnotation(
            value = "被删除的学生姓名是...", category = LogCategoryType.OPERATING,
            type = LogType.DELETE, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#ids}}",
            detail = "学生姓名：「{STUDENT_NAME_BY_IDS{#ids}}」"
    )
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        studentService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param dto a {@link StudentDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @PutMapping
    @ApiOperation(value = "学生-修改", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:edit')")
    @LogRecordAnnotation(
            value = "被修改的学生姓名：「{{#dto.name}}」", category = LogCategoryType.OPERATING,
            type = LogType.UPDATE, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#dto.id}}",
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
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "学生-查询单个", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public ResultWrapper<StudentVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.studentService.getVoById(id));
    }

    /**
     * <p>download.</p>
     *
     * @param criteria a {@link StudentQueryCriteriaDTO} object.
     */
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "学生-导出", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public void download(StudentQueryCriteriaDTO criteria) {
        studentService.download(criteria);
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page   a {@link PageParam} object.
     * @param query  a {@link StudentQueryCriteriaDTO} object.
     * @param course a {@link CourseEnum} object.
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public ResultWrapper<MyPage<StudentVO>> page(PageParam page,
                                                 @ApiParam(value = "课程")
                                                 @RequestParam(required = false) CourseEnum course,
                                                 @Validated StudentQueryCriteriaDTO query) {
        return ResultWrapper.ok(studentService.page(page, course, query));
    }

    @AnonymousGetMapping("/test")
    public ResultWrapper<MyPage<StudentVO>> test(PageParam page,
                                                 @ApiParam(value = "课程")
                                                 @RequestParam(required = false) CourseEnum course,
                                                 @Validated StudentQueryCriteriaDTO query) {
        return ResultWrapper.ok(studentService.page(page, course, query));
    }
}
