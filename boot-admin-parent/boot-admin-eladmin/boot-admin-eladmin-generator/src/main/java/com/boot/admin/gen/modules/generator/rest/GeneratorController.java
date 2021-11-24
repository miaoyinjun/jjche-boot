package com.boot.admin.gen.modules.generator.rest;

import cn.hutool.core.lang.Assert;
import com.boot.admin.gen.modules.generator.domain.ColumnInfoDO;
import com.boot.admin.gen.modules.generator.service.GenConfigService;
import com.boot.admin.gen.modules.generator.service.GeneratorService;
import com.boot.admin.common.constant.EnvConstant;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.util.SpringContextHolder;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>GeneratorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-02
 */
@RequiredArgsConstructor
@SysRestController("generator")
@Api(tags = "系统：代码生成管理")
public class GeneratorController extends BaseController {

    private final GeneratorService generatorService;
    private final GenConfigService genConfigService;

    /**
     * <p>queryTables.</p>
     *
     * @param name      /
     * @param page /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询数据库数据")
    @GetMapping(value = "/tables")
    public ResultWrapper<Object> queryTables(@RequestParam(defaultValue = "") String name,
                                             PageParam page) {
        return ResultWrapper.ok(generatorService.getTables(name, page));
    }

    /**
     * <p>queryColumns.</p>
     *
     * @param tableName a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询字段数据")
    @GetMapping(value = "/columns")
    public ResultWrapper<MyPage> queryColumns(@RequestParam String tableName) {
        List<ColumnInfoDO> columnInfos = generatorService.getColumns(tableName);
        MyPage myPage = new MyPage();
        myPage.setRecords(columnInfos);
        myPage.setTotal(columnInfos.size());
        return ResultWrapper.ok(myPage);
    }

    /**
     * <p>save.</p>
     *
     * @param columnInfos a {@link java.util.List} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("保存字段数据")
    @PutMapping
    public ResultWrapper save(@RequestBody List<ColumnInfoDO> columnInfos) {
        generatorService.saveColumnInfo(columnInfos);
        return ResultWrapper.ok();
    }

    /**
     * <p>sync.</p>
     *
     * @param tables a {@link java.util.List} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("同步字段数据")
    @PostMapping(value = "sync")
    public ResultWrapper sync(@RequestBody List<String> tables) {
        for (String table : tables) {
            generatorService.sync(generatorService.getColumns(table), generatorService.query(table));
        }
        return ResultWrapper.ok();
    }

    /**
     * <p>generator.</p>
     *
     * @param tableName a {@link java.lang.String} object.
     * @param type      a {@link java.lang.Integer} object.
     * @param request   a {@link javax.servlet.http.HttpServletRequest} object.
     * @param response  a {@link javax.servlet.http.HttpServletResponse} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("生成代码")
    @PostMapping(value = "/{tableName}/{type}")
    public ResultWrapper generator(@PathVariable String tableName, @PathVariable Integer type, HttpServletRequest request, HttpServletResponse response) {
        boolean isDev = SpringContextHolder.getEnvActive().equalsIgnoreCase(EnvConstant.DEV);
        Assert.isFalse(!isDev && type == 0, "此环境不允许生成代码，请选择预览或者下载查看！");
        switch (type) {
            // 生成代码
            case 0:
                generatorService.generator(genConfigService.find(tableName), generatorService.getColumns(tableName));
                break;
            // 预览
            case 1:
                List<Map<String, Object>> result = generatorService.preview(genConfigService.find(tableName), generatorService.getColumns(tableName));
                return ResultWrapper.ok(result);
            // 打包
            case 2:
                generatorService.download(genConfigService.find(tableName), generatorService.getColumns(tableName), request, response);
                break;
            default:
                throw new IllegalArgumentException("没有这个选项");
        }
        return ResultWrapper.ok();
    }
}
