package com.boot.admin.system.modules.permissiondatarule.rest;


import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleRoleDTO;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleVO;
import com.boot.admin.system.modules.permissiondatarule.service.SysDataPermissionRuleRoleService;
import com.boot.admin.system.modules.permissiondatarule.service.SysDataPermissionRuleService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 数据规则权限 控制器
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-01
 */
@Api(tags = "数据规则权限")
@ApiSupport(order = 1, author = "miaoyj")
@AdminRestController("sys_data_permission_rule_roles")
@RequiredArgsConstructor
public class SysDataPermissionRuleRoleController extends BaseController {

    private final SysDataPermissionRuleRoleService sysDataPermissionRuleRoleService;
    private final SysDataPermissionRuleService sysDataPermissionRuleService;

    @PostMapping
    @ApiOperation(value = "数据规则权限-保存")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "保存",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据规则权限"
    )
    public ResultWrapper create(@Validated @RequestBody SysDataPermissionRuleRoleDTO dto) {
        sysDataPermissionRuleRoleService.save(dto);
        return ResultWrapper.ok();
    }

    @GetMapping
    @ApiOperation(value = "数据规则权限-列表")
    @PreAuthorize("@el.check('sysDataPermissionRule:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "数据规则权限"
    )
    public ResultWrapper<MyPage<SysDataPermissionRuleVO>> pageQuery(PageParam page,
                                                                    @Validated SysDataPermissionRuleRoleQueryCriteriaDTO query) {
        return ResultWrapper.ok(sysDataPermissionRuleService.pageByMenuIdAndRoleId(page, query));
    }

}