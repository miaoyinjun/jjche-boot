package org.jjche.system.modules.version.rest;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.jjche.system.modules.version.dto.VersionDTO;
import org.jjche.system.modules.version.dto.VersionQueryCriteriaDTO;
import org.jjche.system.modules.version.service.VersionService;
import org.jjche.system.modules.version.vo.VersionVO;
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
     * @param dto a {@link VersionDTO} object.
     * @return a {@link ResultWrapper} object.
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
     * @param dto a {@link VersionDTO} object.
     * @return a {@link ResultWrapper} object.
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
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('version:list')")
    public ResultWrapper<VersionVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.versionService.getVoById(id));
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param query a {@link VersionQueryCriteriaDTO} object.
     * @param page  page
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping
    @PreAuthorize("@el.check('version:list')")
    public ResultWrapper<MyPage<VersionVO>> pageQuery(VersionQueryCriteriaDTO query, PageParam page) {
        return ResultWrapper.ok(versionService.queryAll(query, page));
    }

    /**
     * <p>activated.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link ResultWrapper} object.
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
     * @return a {@link ResultWrapper} object.
     */
    @AnonymousGetMapping("/latest")
    @Cached(name = "versions:", key = "'latest'", cacheType = CacheType.REMOTE)
    public ResultWrapper<String> versionLatest() {
        return ResultWrapper.ok(versionService.versionLatest());
    }
}
