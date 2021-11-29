package com.boot.admin.system.modules.version.rest;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.boot.admin.system.modules.version.dto.VersionQueryCriteriaDTO;
import com.boot.admin.system.modules.version.service.VersionService;
import com.boot.admin.system.modules.version.dto.VersionDTO;
import com.boot.admin.system.modules.version.vo.VersionVO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.security.annotation.rest.AnonymousGetMapping;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 版本 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Api(tags = "系统：版本")
@SysRestController("versions")
@RequiredArgsConstructor
public class VersionController extends BaseController {

    private final VersionService versionService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link com.boot.admin.system.modules.version.dto.VersionDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PostMapping
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "版本"
    )
    @PreAuthorize("@el.check('version:add')")
    public ResultWrapper create(@Validated @RequestBody VersionDTO dto) {
        versionService.save(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param dto a {@link com.boot.admin.system.modules.version.dto.VersionDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PutMapping
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "版本"
    )
    @PreAuthorize("@el.check('version:edit')")
    public ResultWrapper update(@Validated(VersionDTO.VersionDtoUpdateValid.class)
                                @RequestBody VersionDTO dto) {
        versionService.update(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping("/{id}")
    @LogRecordAnnotation(
            value = "查询单个", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "版本"
    )
    @PreAuthorize("@el.check('version:list')")
    public ResultWrapper<VersionVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.versionService.getVoById(id));
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param query a {@link com.boot.admin.system.modules.version.dto.VersionQueryCriteriaDTO} object.
     * @param page  page
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "版本"
    )
    @PreAuthorize("@el.check('version:list')")
    public ResultWrapper<MyPage<VersionVO>> pageQuery(VersionQueryCriteriaDTO query, PageParam page) {
        return ResultWrapper.ok(versionService.queryAll(query, page));
    }

    /**
     * <p>activated.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PutMapping("/{id}")
    @CacheInvalidate(name = "versions:", key = "'latest'")
    @LogRecordAnnotation(
            value = "激活", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "版本"
    )
    @PreAuthorize("@el.check('version:edit')")
    public ResultWrapper activated(@PathVariable Long id) {
        versionService.updateActivated(id);
        return ResultWrapper.ok();
    }

    /**
     * <p>versionLatest.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @AnonymousGetMapping("/latest")
    @Cached(name = "versions:", key = "'latest'", cacheType = CacheType.REMOTE)
    public ResultWrapper<String> versionLatest() {
        return ResultWrapper.ok(versionService.versionLatest());
    }
}
