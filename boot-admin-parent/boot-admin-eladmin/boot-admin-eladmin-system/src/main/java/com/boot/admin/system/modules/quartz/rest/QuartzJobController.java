package com.boot.admin.system.modules.quartz.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.system.modules.quartz.domain.QuartzJobDO;
import com.boot.admin.system.modules.quartz.domain.QuartzLogDO;
import com.boot.admin.system.modules.quartz.dto.JobQueryCriteriaDTO;
import com.boot.admin.system.modules.quartz.service.QuartzJobService;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>QuartzJobController class.</p>
 *
 * @author Zheng Jie
 * @since 2019-01-07
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "系统：定时任务管理")
@SysRestController("jobs")
public class QuartzJobController extends BaseController {

    private static final String ENTITY_NAME = "quartzJob";
    private final QuartzJobService quartzJobService;

    /**
     * <p>query.</p>
     *
     * @param criteria a query object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "定时"
    )
    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    public ResultWrapper<MyPage<QuartzJobDO>> query(JobQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(quartzJobService.queryAll(criteria, pageable));
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.quartz.dto.JobQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "定时"
    )
    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    public void download(HttpServletResponse response, JobQueryCriteriaDTO criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    /**
     * <p>downloadLog.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.quartz.dto.JobQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出任务日志", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "定时"
    )
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    public void downloadLog(HttpServletResponse response, JobQueryCriteriaDTO criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    /**
     * <p>queryJobLog.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.quartz.dto.JobQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public ResultWrapper<MyPage<QuartzLogDO>> queryJobLog(JobQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(quartzJobService.queryAllLog(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.quartz.domain.QuartzJobDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "定时"
    )
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    public ResultWrapper create(@Validated @RequestBody QuartzJobDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        quartzJobService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.quartz.domain.QuartzJobDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    public ResultWrapper update(@Validated @RequestBody QuartzJobDO resources) {
        quartzJobService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "更改定时任务状态", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResultWrapper update(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return ResultWrapper.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询单个", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "定时"
    )
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('timing:list')")
    public ResultWrapper<QuartzJobDO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(quartzJobService.findById(id));
    }

    /**
     * <p>execution.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "执行", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public ResultWrapper execution(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.findById(id));
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "定时"
    )
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        quartzJobService.delete(ids);
        return ResultWrapper.ok();
    }
}
