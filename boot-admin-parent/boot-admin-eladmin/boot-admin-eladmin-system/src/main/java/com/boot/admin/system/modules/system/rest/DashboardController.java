package com.boot.admin.system.modules.system.rest;

import com.boot.admin.system.modules.system.service.DashboardService;
import com.boot.admin.system.modules.system.api.vo.DashboardChartVO;
import com.boot.admin.system.modules.system.api.vo.DashboardCountVO;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>MonitorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-05-02
 */
@RequiredArgsConstructor
@Api(tags = "系统-首页")
@SysRestController("dashboard")
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;

    /**
     * <p>pvIncr.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("首页-记录pv")
    @PostMapping("/pvIncr")
    public ResultWrapper pvIncr() {
        dashboardService.pvIncr();
        return ResultWrapper.ok();
    }

    /**
     * <p>query.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("首页-统计")
    @GetMapping("/count")
    @PreAuthorize("@el.check('dashboard:list')")
    public ResultWrapper<DashboardCountVO> count() {
        return ResultWrapper.ok(dashboardService.count());
    }


    /**
     * <p>chart.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("首页-图表")
    @GetMapping("/chart")
    @PreAuthorize("@el.check('dashboard:list')")
    public ResultWrapper<DashboardChartVO> chart() {
        return ResultWrapper.ok(dashboardService.chart());
    }
}
