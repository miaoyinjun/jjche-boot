package com.boot.admin.system.modules.permissiondatarule.rest;

import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleDTO;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleQueryCriteriaDTO;
import com.boot.admin.system.modules.permissiondatarule.api.enums.SysDataPermissionRuleSortEnum;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleVO;
import com.boot.admin.system.modules.permissiondatarule.service.SysDataPermissionRuleService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
* <p>
* 数据规则 控制器
* </p>
*
* @author miaoyj
* @since 2021-10-27
*/
@Api(tags = "数据规则")
@ApiSupport(order = 1, author = "miaoyj")
@AdminRestController("sys_data_permission_rules")
@RequiredArgsConstructor
public class SysDataPermissionRuleController extends BaseController{

    private final SysDataPermissionRuleService sysDataPermissionRuleService;

    @PostMapping
    @ApiOperation(value = "数据规则-新增")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
              value = "新增",
              category = LogCategoryType.MANAGER,
              type = LogType.ADD, module = "数据规则"
    )
    public ResultWrapper create(@Validated @RequestBody SysDataPermissionRuleDTO dto){
        sysDataPermissionRuleService.save(dto);
        return ResultWrapper.ok();
    }

    @DeleteMapping
    @ApiOperation(value = "数据规则-删除")
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "数据规则",
            prefix = "sysDataPermissionRule", bizNo = "{{#ids}}"
    )
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        sysDataPermissionRuleService.delete(ids);
        return ResultWrapper.ok();
    }

    @PutMapping
    @ApiOperation(value = "数据规则-修改")
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据规则"
    )
    public ResultWrapper update(@Validated(SysDataPermissionRuleDTO.Update.class)
                                            @RequestBody SysDataPermissionRuleDTO dto){
        sysDataPermissionRuleService.update(dto);
        return ResultWrapper.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "数据规则-查询单个")
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "查询单个", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "数据规则"
    )
    public ResultWrapper<SysDataPermissionRuleVO> getById(@PathVariable Long id) {
        return ResultWrapper.ok(this.sysDataPermissionRuleService.getVoById(id));
    }

    @GetMapping
    @ApiOperation(value = "数据规则-列表")
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "数据规则"
    )
    public ResultWrapper<MyPage<SysDataPermissionRuleVO>> pageQuery(PageParam page,
                            @ApiParam(value = "排序", required = true)
                            @NotNull(message = "排序字段不正确")
                            @RequestParam SysDataPermissionRuleSortEnum sort,
                            @Validated SysDataPermissionRuleQueryCriteriaDTO query){
        return ResultWrapper.ok(sysDataPermissionRuleService.pageQuery(page, sort, query));
    }

}