package com.boot.admin.system.modules.mnt.rest;

import com.boot.admin.system.modules.mnt.dto.DeployHistoryDTO;
import com.boot.admin.system.modules.mnt.dto.DeployHistoryQueryCriteriaDTO;
import com.boot.admin.system.modules.mnt.service.DeployHistoryService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>DeployHistoryController class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "运维：部署历史管理")
@AdminRestController("deployHistory")
public class DeployHistoryController extends BaseController {

    private final DeployHistoryService deployhistoryService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.DeployHistoryQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部署历史"
    )
    @ApiOperation("导出部署历史数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public void download(HttpServletResponse response, DeployHistoryQueryCriteriaDTO criteria) throws IOException {
        deployhistoryService.download(deployhistoryService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.mnt.dto.DeployHistoryQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部署历史"
    )
    @ApiOperation(value = "查询部署历史")
    @GetMapping
    @PreAuthorize("@el.check('deployHistory:list')")
    public ResultWrapper<MyPage<DeployHistoryDTO>> query(DeployHistoryQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(deployhistoryService.queryAll(criteria, pageable));
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "部署历史"
    )
    @ApiOperation(value = "删除部署历史")
    @DeleteMapping
    @PreAuthorize("@el.check('deployHistory:del')")
    public ResultWrapper delete(@RequestBody Set<String> ids) {
        deployhistoryService.delete(ids);
        return ResultWrapper.ok();
    }
}
