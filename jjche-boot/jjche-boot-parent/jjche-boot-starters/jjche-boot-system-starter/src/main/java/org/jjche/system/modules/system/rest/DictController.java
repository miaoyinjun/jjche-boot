package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.system.modules.system.api.dto.DictDTO;
import org.jjche.system.modules.system.api.dto.DictQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.DictDO;
import org.jjche.system.modules.system.service.DictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>DictController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@SysRestController("dict")
public class DictController extends BaseController {

    private static final String ENTITY_NAME = "dict";
    private final DictService dictService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link DictQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, DictQueryCriteriaDTO criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }

    /**
     * <p>queryAll.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('dict:list')")
    public ResultWrapper<List<DictDTO>> queryAll() {
        return ResultWrapper.ok(dictService.queryAll(new DictQueryCriteriaDTO()));
    }

    /**
     * <p>query.</p>
     *
     * @param resources a {@link DictQueryCriteriaDTO} object.
     * @param pageable  /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询字典")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResultWrapper<MyPage> query(DictQueryCriteriaDTO resources, PageParam pageable) {
        return ResultWrapper.ok(dictService.queryAll(resources, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DictDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "字典"
    )
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResultWrapper create(@Validated @RequestBody DictDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        dictService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DictDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "字典"
    )
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResultWrapper update(@Validated @RequestBody DictDO resources) {
        dictService.update(resources);
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
            type = LogType.DELETE, module = "字典"
    )
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        dictService.delete(ids);
        return ResultWrapper.ok();
    }
}
