package com.boot.admin.gen.modules.generator.rest;

import com.boot.admin.gen.modules.generator.domain.GenConfigDO;
import com.boot.admin.gen.modules.generator.service.GenConfigService;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>GenConfigController class.</p>
 *
 * @author Zheng Jie
 * @since 2019-01-14
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@SysRestController("genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController extends BaseController {

    private final GenConfigService genConfigService;

    /**
     * <p>query.</p>
     *
     * @param tableName a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询")
    @GetMapping(value = "/{tableName}")
    public ResultWrapper<GenConfigDO> query(@PathVariable String tableName) {
        return ResultWrapper.ok(genConfigService.find(tableName));
    }

    /**
     * <p>update.</p>
     *
     * @param genConfig a {@link com.boot.admin.gen.modules.generator.domain.GenConfigDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("修改")
    @PutMapping
    public ResultWrapper<GenConfigDO> update(@Validated @RequestBody GenConfigDO genConfig) {
        return ResultWrapper.ok(genConfigService.update(genConfig));
    }
}
