package ${packageService}.rest;

import ${packageApi}.vo.${className}VO;
import ${packageService}.service.${className}Service;
import ${packageApi}.dto.${className}QueryCriteriaDTO;
import ${packageApi}.dto.${className}DTO;
import ${packagePath}.constant.ApiVersion;

import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.common.param.PageParam;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.jjche.common.param.MyPage;
import org.springframework.http.MediaType;
import java.util.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import javax.validation.constraints.NotNull;
import org.jjche.common.enums.LogType;
import org.jjche.common.enums.LogCategoryType;
import javax.validation.Valid;

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
              type = LogType.ADD, module = "${apiAlias}", bizNo = "{{#_ret.data}}"
    )
    public ResultWrapper<Long> create(@Validated @Valid @RequestBody ${className}DTO dto){
        return ResultWrapper.ok(${changeClassName}Service.save(dto));
    }

    @DeleteMapping
    @ApiOperation(value = "${apiAlias}-删除", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:del')")
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "${apiAlias}", bizNo = "{{#ids}}"
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
            type = LogType.UPDATE, module = "${apiAlias}", bizNo = "{{#dto.id}}"
    )
    public ResultWrapper update(@Validated(${className}DTO.Update.class)
                                @Valid @RequestBody ${className}DTO dto){
        ${changeClassName}Service.update(dto);
        return ResultWrapper.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "${apiAlias}-查询单个", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public ResultWrapper<${className}VO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.${changeClassName}Service.getVoById(id));
    }

    @ApiOperation(value = "${apiAlias}-导出", tags = ApiVersion.${apiVersionConstant})
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public void download(@Validated ${className}QueryCriteriaDTO criteria) {
        ${changeClassName}Service.download(criteria);
    }

    @GetMapping
    @ApiOperation(value = "${apiAlias}-列表", tags = ApiVersion.${apiVersionConstant})
    @PreAuthorize("@el.check('${changeClassName}:list')")
    public ResultWrapper<MyPage<${className}VO>> pageQuery(PageParam page,
                            @Validated ${className}QueryCriteriaDTO query){
        return ResultWrapper.ok(${changeClassName}Service.page(page, query));
    }

}