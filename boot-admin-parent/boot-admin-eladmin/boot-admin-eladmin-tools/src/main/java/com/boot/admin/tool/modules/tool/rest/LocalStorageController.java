package com.boot.admin.tool.modules.tool.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.tool.modules.tool.domain.LocalStorageDO;
import com.boot.admin.tool.modules.tool.dto.LocalStorageDTO;
import com.boot.admin.tool.modules.tool.dto.LocalStorageQueryCriteriaDTO;
import com.boot.admin.tool.modules.tool.service.LocalStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>LocalStorageController class.</p>
 *
 * @author Zheng Jie
 * @since 2019-09-05
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "工具：本地存储管理")
@AdminRestController("localStorage")
public class LocalStorageController extends BaseController {

    private final LocalStorageService localStorageService;

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.tool.modules.tool.dto.LocalStorageQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询文件")
    @GetMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResultWrapper<MyPage<LocalStorageDTO>> query(LocalStorageQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(localStorageService.queryAll(criteria, pageable));
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.tool.modules.tool.dto.LocalStorageQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "存储"
    )
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void download(HttpServletResponse response, LocalStorageQueryCriteriaDTO criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    /**
     * <p>create.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param file a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("上传文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:add')")
    public ResultWrapper create(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.create(name, file);
        return ResultWrapper.ok();
    }

    /**
     * <p>upload.</p>
     *
     * @param file a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "上传图片", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "存储"
    )
    @PostMapping("/pictures")
    @ApiOperation("上传图片")
    public ResultWrapper<LocalStorageDO> upload(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        Boolean isPic = FileUtil.IMAGE.equals(FileUtil.getFileType(suffix));
        Assert.isTrue(isPic, "只能上传图片");
        LocalStorageDO localStorage = localStorageService.create(null, file);
        return ResultWrapper.ok(localStorage);
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.tool.modules.tool.domain.LocalStorageDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("修改文件")
    @PutMapping
    @PreAuthorize("@el.check('storage:edit')")
    public ResultWrapper update(@Validated @RequestBody LocalStorageDO resources) {
        localStorageService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids an array of {@link java.lang.Long} objects.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "多选删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "存储"
    )
    @DeleteMapping
    @ApiOperation("多选删除")
    public ResultWrapper delete(@RequestBody List<Long> ids) {
        localStorageService.deleteAll(ids);
        return ResultWrapper.ok();
    }
}
