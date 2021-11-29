package com.boot.admin.system.modules.mnt.rest;

import cn.hutool.log.StaticLog;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.mnt.domain.DeployDO;
import com.boot.admin.system.modules.mnt.domain.DeployHistoryDO;
import com.boot.admin.system.modules.mnt.dto.DeployDTO;
import com.boot.admin.system.modules.mnt.dto.DeployQueryCriteriaDTO;
import com.boot.admin.system.modules.mnt.service.DeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * <p>DeployController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Api(tags = "运维：部署管理")
@RequiredArgsConstructor
@SysRestController("deploy")
public class DeployController extends BaseController {

    private final String fileSavePath = FileUtil.getTmpDirPath() + "/";
    private final DeployService deployService;


    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.DeployQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部署"
    )
    @ApiOperation("导出部署数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('database:list')")
    public void download(HttpServletResponse response, DeployQueryCriteriaDTO criteria) throws IOException {
        deployService.download(deployService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.DeployQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部署"
    )
    @ApiOperation(value = "查询部署")
    @GetMapping
    @PreAuthorize("@el.check('deploy:list')")
    public ResultWrapper<MyPage<DeployDTO>> query(DeployQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(deployService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "部署"
    )
    @ApiOperation(value = "新增部署")
    @PostMapping
    @PreAuthorize("@el.check('deploy:add')")
    public ResultWrapper create(@Validated @RequestBody DeployDO resources) {
        deployService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "部署"
    )
    @ApiOperation(value = "修改部署")
    @PutMapping
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper update(@Validated @RequestBody DeployDO resources) {
        deployService.update(resources);
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
            type = LogType.DELETE, module = "部署"
    )
    @ApiOperation(value = "删除部署")
    @DeleteMapping
    @PreAuthorize("@el.check('deploy:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        deployService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>upload.</p>
     *
     * @param file    a {@link org.springframework.web.multipart.MultipartFile} object.
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "上传文件", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "部署"
    )
    @ApiOperation(value = "上传文件部署")
    @PostMapping(value = "/upload")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper<String> upload(@RequestBody MultipartFile file, HttpServletRequest request) throws Exception {
        Long id = Long.valueOf(request.getParameter("id"));
        String fileName = "";
        if (file != null) {
            fileName = file.getOriginalFilename();
            File deployFile = new File(fileSavePath + fileName);
            FileUtil.del(deployFile);
            file.transferTo(deployFile);
            //文件下一步要根据文件名字来
            deployService.deployApp(fileSavePath + fileName, id);
        } else {
            StaticLog.warn("没有找到相对应的文件");
        }
        return ResultWrapper.ok(fileName);
    }

    /**
     * <p>serverReduction.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployHistoryDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "系统还原", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "部署"
    )
    @ApiOperation(value = "系统还原")
    @PostMapping(value = "/serverReduction")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper<String> serverReduction(@Validated @RequestBody DeployHistoryDO resources) {
        String result = deployService.serverReduction(resources);
        return ResultWrapper.ok(result);
    }

    /**
     * <p>serverStatus.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "服务运行状态", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部署"
    )
    @ApiOperation(value = "服务运行状态")
    @PostMapping(value = "/serverStatus")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper<String> serverStatus(@Validated @RequestBody DeployDO resources) {
        String result = deployService.serverStatus(resources);
        return ResultWrapper.ok(result);
    }

    /**
     * <p>startServer.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "启动服务", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "部署"
    )
    @ApiOperation(value = "启动服务")
    @PostMapping(value = "/startServer")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper<String> startServer(@Validated @RequestBody DeployDO resources) {
        String result = deployService.startServer(resources);
        return ResultWrapper.ok(result);
    }

    /**
     * <p>stopServer.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.mnt.domain.DeployDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "停止服务", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "部署"
    )
    @ApiOperation(value = "停止服务")
    @PostMapping(value = "/stopServer")
    @PreAuthorize("@el.check('deploy:edit')")
    public ResultWrapper<String> stopServer(@Validated @RequestBody DeployDO resources) {
        String result = deployService.stopServer(resources);
        return ResultWrapper.ok(result);
    }
}
