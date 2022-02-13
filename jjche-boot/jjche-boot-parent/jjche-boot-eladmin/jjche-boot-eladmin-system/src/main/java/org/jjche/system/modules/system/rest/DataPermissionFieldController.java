package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldVO;
import org.jjche.system.modules.system.service.DataPermissionFieldService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>MenuController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */

@RequiredArgsConstructor
@Api(tags = "系统：数据字段管理")
@SysRestController("data_permission_fields")
public class DataPermissionFieldController extends BaseController {

    private final DataPermissionFieldService dataPermissionFieldService;

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DataPermissionFieldDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据字段"
    )
    @ApiOperation("新增数据字段")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResultWrapper create(@Validated @RequestBody DataPermissionFieldDTO resources) {
        Assert.isNull(resources.getId(), "A new DataPermissionFieldDO cannot already have an ID");
        dataPermissionFieldService.create(resources);
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
            type = LogType.DELETE, module = "数据字段"
    )
    @ApiOperation("删除数据字段")
    @DeleteMapping()
    @PreAuthorize("@el.check('menu:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        dataPermissionFieldService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DataPermissionFieldDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据字段"
    )
    @ApiOperation("修改数据字段")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResultWrapper update(@Validated(DataPermissionFieldDTO.Update.class) @RequestBody DataPermissionFieldDTO resources) {
        dataPermissionFieldService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "数据字段-查询单个")
    @PreAuthorize("@el.check('menu:list')")
    public ResultWrapper<DataPermissionFieldVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.dataPermissionFieldService.getVoById(id));
    }


    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DataPermissionFieldQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询数据字段")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResultWrapper<MyPage<DataPermissionFieldVO>> query(PageParam pageable, DataPermissionFieldQueryCriteriaDTO criteria) {
        return ResultWrapper.ok(dataPermissionFieldService.pageQuery(pageable, criteria));
    }
}
