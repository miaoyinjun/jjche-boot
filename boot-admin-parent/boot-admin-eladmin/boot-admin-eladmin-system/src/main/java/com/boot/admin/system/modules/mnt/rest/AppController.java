package com.boot.admin.system.modules.mnt.rest;

import com.boot.admin.system.modules.mnt.domain.AppDO;
import com.boot.admin.system.modules.mnt.dto.AppDTO;
import com.boot.admin.system.modules.mnt.dto.AppQueryCriteriaDTO;
import com.boot.admin.system.modules.mnt.service.AppService;
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
 * <p>AppController class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "运维：应用管理")
@SysRestController("app")
public class AppController extends BaseController {

    private final AppService appService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.AppQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "应用"
    )
    @ApiOperation("导出应用数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('app:list')")
    public void download(HttpServletResponse response, AppQueryCriteriaDTO criteria) throws IOException {
        appService.download(appService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.AppQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "应用"
    )
    @ApiOperation(value = "查询应用")
    @GetMapping
    @PreAuthorize("@el.check('app:list')")
    public ResultWrapper<MyPage<AppDTO>> query(AppQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(appService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.AppDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "应用"
    )
    @ApiOperation(value = "新增应用")
    @PostMapping
    @PreAuthorize("@el.check('app:add')")
    public ResultWrapper create(@Validated @RequestBody AppDO resources) {
        appService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.AppDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "应用"
    )
    @ApiOperation(value = "修改应用")
    @PutMapping
    @PreAuthorize("@el.check('app:edit')")
    public ResultWrapper update(@Validated @RequestBody AppDO resources) {
        appService.update(resources);
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
            type = LogType.DELETE, module = "应用"
    )
    @ApiOperation(value = "删除应用")
    @DeleteMapping
    @PreAuthorize("@el.check('app:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        appService.delete(ids);
        return ResultWrapper.ok();
    }
}
