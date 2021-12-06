package com.boot.admin.log.modules.logging.rest;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.modules.logging.dto.LogQueryCriteriaDTO;
import com.boot.admin.log.modules.logging.service.LogService;
import com.boot.admin.log.modules.logging.vo.LogVO;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>LogController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
@RequiredArgsConstructor
@SysRestController("logs")
@Api(tags = "系统：日志管理")
public class LogController extends BaseController {

    private final LogService logService;

    /**
     * <p>
     * download
     * </p>
     *
     * @param response 返回
     * @param criteria 条件
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('log:list')")
    public void download(HttpServletResponse response, LogQueryCriteriaDTO criteria) throws IOException {
        logService.download(logService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.log.modules.logging.dto.LogQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping
    @PreAuthorize("@el.check('log:list')")
    public ResultWrapper<MyPage<LogVO>> query(LogQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(logService.queryAll(criteria, pageable));
    }

    /**
     * <p>queryUserLog.</p>
     *
     * @param criteria a {@link com.boot.admin.log.modules.logging.dto.LogQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public ResultWrapper<MyPage<LogVO>> queryUserLog(LogQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(logService.queryAllByUser(criteria, pageable));
    }

    /**
     * <p>queryErrorLogs.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('log:list')")
    public ResultWrapper<Object> queryErrorLogs(@PathVariable Long id) {
        return ResultWrapper.ok(logService.findByErrDetail(id));
    }

    /**
     * <p>delAllInfoLog.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @DeleteMapping(value = "/del")
    @LogRecordAnnotation(
            value = "清空6个月之前的操作日志", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "日志"
    )
    @ApiOperation("清空6个月之前的操作日志")
    @PreAuthorize("@el.check('log:del')")
    public ResultWrapper delAllInfoLog() {
        logService.delAll();
        return ResultWrapper.ok();
    }

    /**
     * <p>listModule.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping("/modules")
    @ApiOperation("日志获取模块")
    @PreAuthorize("@el.check('log:list')")
    @Cached(name = "logs:modules", cacheType = CacheType.REMOTE, expire = 3600000)
    public ResultWrapper<List<String>> listModule() {
        return ResultWrapper.ok(logService.listModule());
    }

    /**
     * <p>listByBizKeyAndBizNo.</p>
     *
     * @param page a {@link com.boot.admin.mybatis.param.PageParam} object.
     * @param bizKey a {@link java.lang.String} object.
     * @param bizNo a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping("/biz")
    @ApiOperation("查询业务标识与业务主键")
    public ResultWrapper<MyPage<LogDO>> listByBizKeyAndBizNo(PageParam page,
                                                           String bizKey, String bizNo) {
        return ResultWrapper.ok(logService.listByBizKeyAndBizNo(page, bizKey, bizNo));
    }

}
