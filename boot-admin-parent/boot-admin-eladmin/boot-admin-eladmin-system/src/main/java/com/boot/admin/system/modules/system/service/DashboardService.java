package com.boot.admin.system.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.boot.admin.log.modules.logging.service.LogService;
import com.boot.admin.system.modules.system.api.vo.DashboardChartVO;
import com.boot.admin.system.modules.system.api.vo.DashboardCountVO;
import com.boot.admin.cache.service.RedisService;
import com.boot.admin.core.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 首页
 * </p>
 *
 * @author miaoyj
 * @since 2021-08-20
 * @version 1.0.0-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class DashboardService {
    private final String DASHBOARD_CACHE_KEY = "dashboard:";
    private final String TOTAL_PV_CACHE_KEY = DASHBOARD_CACHE_KEY + "total_pv_cache_key";
    private final String TODAY_PV_CACHE_KEY = DASHBOARD_CACHE_KEY + "today_pv_cache_key";

    private final RedisService redisService;
    private final UserService userService;
    private final LogService logService;

    /**
     * <p>
     * pv统计
     * </p>
     */
    public void pvIncr() {
        redisService.stringIncrementLongString(TOTAL_PV_CACHE_KEY, 1L);
        redisService.hashIncrementLongOfHashMap(TODAY_PV_CACHE_KEY, DateUtil.today(), 1L);
    }

    /**
     * <p>
     * 首页统计
     * </p>
     *
     * @return /
     */
    public DashboardCountVO count() {
        DashboardCountVO dashboardCountVO = new DashboardCountVO();
        dashboardCountVO.setTotalUserCount(userService.count());
        dashboardCountVO.setTodayUserCount(userService.countTodayUser());

        dashboardCountVO.setTotalPv(redisService.objectGetObject(TOTAL_PV_CACHE_KEY, Integer.class));
        dashboardCountVO.setTodayPv(redisService.hashGet(TODAY_PV_CACHE_KEY, DateUtil.today(), Integer.class));

        dashboardCountVO.setTotalLoginPv(logService.getTotalLoginPv());
        dashboardCountVO.setTodayLoginPv(logService.getTodayLoginPv());

        dashboardCountVO.setTotalLoginIv(logService.getTotalLoginIv());
        dashboardCountVO.setTodayLoginIv(logService.getTodayLoginIv());

        return dashboardCountVO;
    }

    /**
     * <p>
     * 首页图表
     * </p>
     *
     * @return /
     */
    public DashboardChartVO chart() {
        DashboardChartVO dashboardChartVO = new DashboardChartVO();
        dashboardChartVO.setLastTenVisitCount(logService. findLastTenDaysVisitCount(null));
        dashboardChartVO.setLastTenUserVisitCount(logService.findLastTenDaysVisitCount(SecurityUtils.getCurrentUsername()));

        dashboardChartVO.setBrowserCount(logService.findByBrowser());
        dashboardChartVO.setOperatingSystemCount(logService.findByOs());
        return dashboardChartVO;
    }

}
