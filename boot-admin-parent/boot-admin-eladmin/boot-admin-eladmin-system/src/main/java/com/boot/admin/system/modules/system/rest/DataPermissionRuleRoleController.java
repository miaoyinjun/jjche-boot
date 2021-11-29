package com.boot.admin.system.modules.system.rest;


import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleVO;
import com.boot.admin.system.modules.system.service.DataPermissionRuleRoleService;
import com.boot.admin.system.modules.system.service.DataPermissionRuleService;
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
 * @version 1.0.1-SNAPSHOT
 */
@Api(tags = "系统：数据规则权限")
@ApiSupport(order = 1, author = "miaoyj")
@SysRestController("data_permission_rule_roles")
@RequiredArgsConstructor
public class DataPermissionRuleRoleController extends BaseController {

    private final DataPermissionRuleRoleService sysDataPermissionRuleRoleService;
    private final DataPermissionRuleService sysDataPermissionRuleService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PostMapping
    @ApiOperation(value = "数据规则权限-保存")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('roles:edit')")
    @LogRecordAnnotation(
            value = "保存",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据规则权限"
    )
    public ResultWrapper create(@Validated @RequestBody DataPermissionRuleRoleDTO dto) {
        sysDataPermissionRuleRoleService.save(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page a {@link com.boot.admin.mybatis.param.PageParam} object.
     * @param query a {@link com.boot.admin.system.modules.system.api.dto.DataPermissionRuleRoleQueryCriteriaDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping
    @ApiOperation(value = "数据规则权限-列表")
    @PreAuthorize("@el.check('roles:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "数据规则权限"
    )
    public ResultWrapper<MyPage<DataPermissionRuleVO>> pageQuery(PageParam page,
                                                                 @Validated DataPermissionRuleRoleQueryCriteriaDTO query) {
        return ResultWrapper.ok(sysDataPermissionRuleService.pageByMenuIdAndRoleId(page, query));
    }
}
