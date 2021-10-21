package com.boot.admin.system.modules.system.rest;

import com.boot.admin.system.modules.system.service.MonitorService;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

/**
 * <p>MonitorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-05-02
 */
@RequiredArgsConstructor
@Api(tags = "系统-服务监控管理")
@AdminRestController("monitor")
public class MonitorController extends BaseController {

    private final MonitorService serverService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * <p>query.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询服务监控")
    @GetMapping("/server")
    @PreAuthorize("@el.check('monitor:list')")
    public ResultWrapper<Object> query() {
        return ResultWrapper.ok(serverService.getServers());
    }

    /**
     * <p>getCache.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("查询缓存监控")
    @GetMapping("/cache")
    @PreAuthorize("@el.check('monitor:list')")
    public ResultWrapper<Object> getCache() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.dbSize());

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return ResultWrapper.ok(result);
    }
}
