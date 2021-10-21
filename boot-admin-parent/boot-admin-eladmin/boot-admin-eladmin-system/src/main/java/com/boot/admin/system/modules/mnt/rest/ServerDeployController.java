package com.boot.admin.system.modules.mnt.rest;

import com.boot.admin.system.modules.mnt.domain.ServerDeployDO;
import com.boot.admin.system.modules.mnt.dto.ServerDeployDTO;
import com.boot.admin.system.modules.mnt.dto.ServerDeployQueryCriteriaDTO;
import com.boot.admin.system.modules.mnt.service.ServerDeployService;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.AdminRestController;
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
 * <p>ServerDeployController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Api(tags = "运维：服务器管理")
@RequiredArgsConstructor
@AdminRestController("serverDeploy")
public class ServerDeployController extends BaseController {

    private final ServerDeployService serverDeployService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.ServerDeployQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "服务器"
    )
    @ApiOperation("导出服务器数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public void download(HttpServletResponse response, ServerDeployQueryCriteriaDTO criteria) throws IOException {
        serverDeployService.download(serverDeployService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.ServerDeployQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "服务器"
    )
    @ApiOperation(value = "查询服务器")
    @GetMapping
    @PreAuthorize("@el.check('serverDeploy:list')")
    public ResultWrapper<MyPage<ServerDeployDTO>> query(
            ServerDeployQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(serverDeployService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.ServerDeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "服务器"
    )
    @ApiOperation(value = "新增服务器")
    @PostMapping
    @PreAuthorize("@el.check('serverDeploy:add')")
    public ResultWrapper create(@Validated @RequestBody ServerDeployDO resources) {
        serverDeployService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.ServerDeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "服务器"
    )
    @ApiOperation(value = "修改服务器")
    @PutMapping
    @PreAuthorize("@el.check('serverDeploy:edit')")
    public ResultWrapper update(@Validated @RequestBody ServerDeployDO resources) {
        serverDeployService.update(resources);
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
            type = LogType.DELETE, module = "服务器"
    )
    @ApiOperation(value = "删除Server")
    @DeleteMapping
    @PreAuthorize("@el.check('serverDeploy:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        serverDeployService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>testConnect.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.ServerDeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "测试连接", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "服务器"
    )
    @ApiOperation(value = "测试连接服务器")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public ResultWrapper<Boolean> testConnect(@Validated @RequestBody ServerDeployDO resources) {
        return ResultWrapper.ok(serverDeployService.testConnect(resources));
    }
}
