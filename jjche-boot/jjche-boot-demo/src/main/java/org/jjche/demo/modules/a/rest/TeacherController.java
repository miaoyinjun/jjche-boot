package org.jjche.demo.modules.a.rest;

import org.jjche.demo.modules.a.api.vo.TeacherVO;
import org.jjche.demo.modules.a.service.TeacherService;
import org.jjche.demo.modules.a.api.dto.TeacherQueryCriteriaDTO;
import org.jjche.demo.modules.a.api.dto.TeacherDTO;
import org.jjche.demo.constant.ApiVersion;
import org.jjche.common.dto.BaseDTO;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.common.param.PageParam;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.jjche.common.param.MyPage;
import org.springframework.http.MediaType;
import java.util.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import org.jjche.common.enums.LogType;
import org.jjche.common.enums.LogCategoryType;
import javax.validation.Valid;
import java.util.List;

/**
* <p>
* ss 控制器
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Api(tags = "ss")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("teachers")
@RequiredArgsConstructor
public class TeacherController extends BaseController{

    private final TeacherService teacherService;

    @PostMapping
    @ApiOperation(value = "ss-新增", tags = ApiVersion.VERSION_1_0_0)
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('teacher:add')")
    @LogRecord(
              value = "新增",
              category = LogCategoryType.OPERATING,
              type = LogType.ADD, module = "ss", bizNo = "{{#_ret.data}}"
    )
    public ResultWrapper<Long> create(@Validated @Valid @RequestBody TeacherDTO dto){
        return ResultWrapper.ok(teacherService.save(dto));
    }

    @DeleteMapping
    @ApiOperation(value = "ss-删除", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('teacher:del')")
    @LogRecord(
            batch = true, value = "删除", category = LogCategoryType.OPERATING,
            type = LogType.DELETE, module = "ss", bizNo = "{{#ids}}",
            detail = "{API_MODEL{#_oldObj}} {TEACHER_DIFF_OLD_BY_ID{#ids}}"
    )
    public ResultWrapper delete(@RequestBody List<Long> ids) {
        teacherService.delete(ids);
        return ResultWrapper.ok();
    }

    @PutMapping
    @ApiOperation(value = "ss-修改", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('teacher:edit')")
    @LogRecord(
            value = "修改", category = LogCategoryType.OPERATING,
            type = LogType.UPDATE, module = "ss", bizNo = "{{#dto.id}}",
            detail = "{_DIFF{#dto}} {TEACHER_DIFF_OLD_BY_ID{#dto.id}}"
    )
    public ResultWrapper update(@Validated(BaseDTO.Update.class)
                                @Valid @RequestBody TeacherDTO dto){
        teacherService.update(dto);
        return ResultWrapper.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "ss-查询单个", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('teacher:list')")
    public ResultWrapper<TeacherVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.teacherService.getVoById(id));
    }

    @ApiOperation(value = "ss-导出", tags = ApiVersion.VERSION_1_0_0)
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("@el.check('teacher:list')")
    public void download(@Validated TeacherQueryCriteriaDTO criteria) {
        teacherService.download(criteria);
    }

    @GetMapping
    @ApiOperation(value = "ss-列表", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('teacher:list')")
    public ResultWrapper<MyPage<TeacherVO>> pageQuery(PageParam page,
                            @Validated TeacherQueryCriteriaDTO query){
        return ResultWrapper.ok(teacherService.page(page, query));
    }

}