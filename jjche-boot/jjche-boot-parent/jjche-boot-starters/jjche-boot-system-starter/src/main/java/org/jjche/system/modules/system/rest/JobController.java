package org.jjche.system.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.system.modules.system.api.dto.JobDTO;
import org.jjche.system.modules.system.api.dto.JobQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.JobDO;
import org.jjche.system.modules.system.service.JobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>JobController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
@RequiredArgsConstructor
@Api(tags = "系统：岗位管理")
@SysRestController("job")
public class JobController extends BaseController {

    private static final String ENTITY_NAME = "job";
    private final JobService jobService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link JobQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('job:list')")
    public void download(HttpServletResponse response, JobQueryCriteriaDTO criteria) throws IOException {
        jobService.download(jobService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link JobQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询岗位")
    @GetMapping
    @PreAuthorize("@el.check('job:list','user:list')")
    public ResultWrapper<MyPage<JobDTO>> query(JobQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(jobService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link JobDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "岗位"
    )
    @ApiOperation("新增岗位")
    @PostMapping
    @PreAuthorize("@el.check('job:add')")
    public ResultWrapper create(@Validated @RequestBody JobDTO resources) {
        jobService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link JobDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "岗位"
    )
    @ApiOperation("修改岗位")
    @PutMapping
    @PreAuthorize("@el.check('job:edit')")
    public ResultWrapper update(@Validated @RequestBody JobDO resources) {
        jobService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "岗位"
    )
    @ApiOperation("删除岗位")
    @DeleteMapping
    @PreAuthorize("@el.check('job:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        // 验证是否被用户关联
        jobService.verification(ids);
        jobService.delete(ids);
        return ResultWrapper.ok();
    }
}
