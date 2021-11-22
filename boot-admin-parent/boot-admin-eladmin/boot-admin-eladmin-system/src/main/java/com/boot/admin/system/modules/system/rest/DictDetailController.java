package com.boot.admin.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.system.modules.system.domain.DictDetailDO;
import com.boot.admin.system.modules.system.api.dto.DictDetailDTO;
import com.boot.admin.system.modules.system.api.dto.DictDetailQueryCriteriaDTO;
import com.boot.admin.system.modules.system.service.DictDetailService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>DictDetailController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@RequiredArgsConstructor
@Api(tags = "系统：字典详情管理")
@AdminRestController("dictDetail")
public class DictDetailController extends BaseController {

    private final DictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.system.api.dto.DictDetailQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询详情", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "字典"
    )
    @ApiOperation("查询字典详情")
    @GetMapping
    public ResultWrapper<MyPage<DictDetailDTO>> query(DictDetailQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(dictDetailService.queryAll(criteria, pageable));
    }

    /**
     * <p>getDictDetailMaps.</p>
     *
     * @param dictName a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询多个字典详情", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "字典"
    )
    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public ResultWrapper<Map<String, List<DictDetailDTO>>> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDTO>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return ResultWrapper.ok(dictMap);
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.DictDetailDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增字典详情", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "字典"
    )
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public ResultWrapper create(@Validated @RequestBody DictDetailDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        dictDetailService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.DictDetailDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "字典"
    )
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public ResultWrapper update(@Validated @RequestBody DictDetailDO resources) {
        dictDetailService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除字典详情", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "字典"
    )
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResultWrapper delete(@PathVariable Long id) {
        dictDetailService.delete(id);
        return ResultWrapper.ok();
    }
}
