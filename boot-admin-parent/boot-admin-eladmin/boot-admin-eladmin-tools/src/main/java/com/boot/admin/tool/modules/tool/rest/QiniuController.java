package com.boot.admin.tool.modules.tool.rest;

import com.boot.admin.tool.modules.tool.domain.QiniuConfigDO;
import com.boot.admin.tool.modules.tool.domain.QiniuContentDO;
import com.boot.admin.tool.modules.tool.dto.QiniuQueryCriteriaDTO;
import com.boot.admin.tool.modules.tool.service.QiNiuService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送邮件
 *
 * @author 郑杰
 * @since 2018/09/28 6:55:53
 * @version 1.0.8-SNAPSHOT
 */
@Slf4j
@RequiredArgsConstructor
@AdminRestController("qiNiuContent")
@Api(tags = "工具：七牛云存储管理")
public class QiniuController extends BaseController {

    private final QiNiuService qiNiuService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping(value = "/config")
    public ResultWrapper<QiniuConfigDO> queryConfig() {
        return ResultWrapper.ok(qiNiuService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param qiniuConfig a {@link com.boot.admin.tool.modules.tool.domain.QiniuConfigDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "配置", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "七牛云存储"
    )
    @ApiOperation("配置七牛云存储")
    @PutMapping(value = "/config")
    public ResultWrapper updateConfig(@Validated @RequestBody QiniuConfigDO qiniuConfig) {
        qiNiuService.config(qiniuConfig);
        qiNiuService.update(qiniuConfig.getType());
        return ResultWrapper.ok();
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.tool.modules.tool.dto.QiniuQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "七牛云存储"
    )
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void download(HttpServletResponse response, QiniuQueryCriteriaDTO criteria) throws IOException {
        qiNiuService.downloadList(qiNiuService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.tool.modules.tool.dto.QiniuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询文件", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "七牛云存储"
    )
    @ApiOperation("查询文件")
    @GetMapping
    public ResultWrapper<MyPage> query(QiniuQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(qiNiuService.queryAll(criteria, pageable));
    }

    /**
     * <p>upload.</p>
     *
     * @param file a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "上传文件", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "七牛云存储"
    )
    @ApiOperation("上传文件")
    @PostMapping
    public ResultWrapper<Object> upload(@RequestParam MultipartFile file) {
        QiniuContentDO qiniuContent = qiNiuService.upload(file, qiNiuService.find());
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return ResultWrapper.ok(map);
    }

    /**
     * <p>synchronize.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "同步", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "七牛云存储"
    )
    @ApiOperation("同步七牛云数据")
    @PostMapping(value = "/synchronize")
    public ResultWrapper synchronize() {
        qiNiuService.synchronize(qiNiuService.find());
        return ResultWrapper.ok();
    }

    /**
     * <p>download.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "下载", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "七牛云存储"
    )
    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{id}")
    public ResultWrapper<Object> download(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiNiuService.download(qiNiuService.findByContentId(id), qiNiuService.find()));
        return ResultWrapper.ok(map);
    }

    /**
     * <p>delete.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "七牛云存储"
    )
    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public ResultWrapper delete(@PathVariable Long id) {
        qiNiuService.delete(qiNiuService.findByContentId(id), qiNiuService.find());
        return ResultWrapper.ok();
    }

    /**
     * <p>deleteAll.</p>
     *
     * @param ids an array of {@link java.lang.Long} objects.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除多张图片", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "七牛云存储"
    )
    @ApiOperation("删除多张图片")
    @DeleteMapping
    public ResultWrapper deleteAll(@RequestBody Long[] ids) {
        qiNiuService.deleteAll(ids, qiNiuService.find());
        return ResultWrapper.ok();
    }
}
