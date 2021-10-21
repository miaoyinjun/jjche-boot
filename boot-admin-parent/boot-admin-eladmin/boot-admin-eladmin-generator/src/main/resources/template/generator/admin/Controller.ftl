package ${packageService}.rest;

import ${packageApi}.vo.${className}VO;
import ${packageService}.service.${className}Service;
import ${packageApi}.dto.${className}QueryCriteriaDTO;
import ${packageApi}.dto.${className}DTO;
import ${packageApi}.enums.${className}SortEnum;
import ${packagePath}.constant.ApiVersion;

import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.core.annotation.controller.ApiRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.mybatis.param.PageParam;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.boot.admin.mybatis.param.MyPage;
import org.springframework.http.MediaType;
import java.util.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.common.enums.LogCategoryType;

/**
* <p>
* ${apiAlias} 控制器
* </p>
*
* @author ${author}
* @since ${date}
*/
@Api(tags = "${apiAlias}")
@ApiSupport(order = 1, author = "${author}")
@ApiRestController("${controllerBaseUrl}")
@RequiredArgsConstructor
public class ${className}Controller extends BaseController{

    private final ${className}Service ${changeClassName}Service;

    @PostMapping
    @ApiOperation(value = "${apiAlias}-新增", tags = ApiVersion.${apiVersionConstant})
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('${changeClassName}:add')")
    @LogRecordAnnotation(
              value = "新增",
              category = LogCategoryType.MANAGER,
              type = LogType.ADD, module = "${apiAlias}"
    )
    public ResultWrapper create(@Validated @RequestBody ${className}DTO dto){
        ${changeClassName}Service.save(dto);
        return ResultWrapper.ok();
    }

    @DeleteMapping
    @ApiOperation(value = "${apiAlias}-删除", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:del')")
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "${apiAlias}",
            prefix = "${changeClassName}", bizNo = "{{#ids}}"
    )
    public ResultWrapper delete(@RequestBody Set<${pkColumnType}> ids) {
        ${changeClassName}Service.delete(ids);
        return ResultWrapper.ok();
    }

    @PutMapping
    @ApiOperation(value = "${apiAlias}-修改", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:edit')")
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "${apiAlias}"
    )
    public ResultWrapper update(@Validated(${className}DTO.Update.class)
                                            @RequestBody ${className}DTO dto){
        ${changeClassName}Service.update(dto);
        return ResultWrapper.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "${apiAlias}-查询单个", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:list')")
    @LogRecordAnnotation(
            value = "查询单个", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "${apiAlias}"
    )
    public ResultWrapper<${className}VO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.${changeClassName}Service.getVoById(id));
    }

    @ApiOperation(value = "${apiAlias}-导出", tags = ApiVersion.${apiVersionConstant})
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("@el.check('${changeClassName}:list')")
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "${apiAlias}"
    )
    public void download(@ApiParam(value = "排序", required = true)
                        @NotNull(message = "排序字段不正确")
                        @RequestParam ${className}SortEnum sort,
                        @Validated ${className}QueryCriteriaDTO criteria) {
        ${changeClassName}Service.download(sort, criteria);
    }

    @GetMapping
    @ApiOperation(value = "${apiAlias}-列表", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "${apiAlias}"
    )
    public ResultWrapper<MyPage<${className}VO>> pageQuery(PageParam page,
                            @ApiParam(value = "排序", required = true)
                            @NotNull(message = "排序字段不正确")
                            @RequestParam ${className}SortEnum sort,
                            @Validated ${className}QueryCriteriaDTO query){
        return ResultWrapper.ok(${changeClassName}Service.pageQuery(page, sort, query));
    }

}