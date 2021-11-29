package com.boot.admin.system.modules.system.rest;

import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldRoleDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.vo.DataPermissionFieldRoleVO;
import com.boot.admin.system.modules.system.service.DataPermissionFieldRoleService;
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
 * 数据字段角色 控制器
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-04
 */
@Api(tags = "系统：数据字段角色")
@ApiSupport(order = 1, author = "miaoyj")
@SysRestController("data_permission_field_roles")
@RequiredArgsConstructor
public class DataPermissionFieldRoleController extends BaseController {

    private final DataPermissionFieldRoleService sysDataPermissionFieldRoleService;

    @PostMapping
    @ApiOperation(value = "数据字段角色-保存")
    @PreAuthorize("@el.check('roles:edit')")
    @LogRecordAnnotation(
            value = "保存",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据字段角色"
    )
    public ResultWrapper create(@Validated @RequestBody DataPermissionFieldRoleDTO dto) {
        sysDataPermissionFieldRoleService.save(dto);
        return ResultWrapper.ok();
    }

    @GetMapping
    @ApiOperation(value = "数据字段角色-列表")
    @PreAuthorize("@el.check('roles:list')")
    @LogRecordAnnotation(
            value = "列表", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "数据字段角色"
    )
    public ResultWrapper<MyPage<DataPermissionFieldRoleVO>> pageQuery(PageParam page,
                                                                      @Validated DataPermissionFieldRoleQueryCriteriaDTO query) {
        return ResultWrapper.ok(sysDataPermissionFieldRoleService.page(page, query));
    }

}