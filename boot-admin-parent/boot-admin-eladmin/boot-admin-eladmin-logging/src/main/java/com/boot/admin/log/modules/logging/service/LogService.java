package com.boot.admin.log.modules.logging.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogModule;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.common.util.ValidationUtil;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.modules.logging.dto.LogQueryCriteriaDTO;
import com.boot.admin.log.modules.logging.mapper.LogMapper;
import com.boot.admin.log.modules.logging.mapstruct.LogMapStruct;
import com.boot.admin.log.modules.logging.vo.DashboardChartBrowserVO;
import com.boot.admin.log.modules.logging.vo.DashboardChartLastTenVisitVO;
import com.boot.admin.log.modules.logging.vo.DashboardChartOperatingSystemVO;
import com.boot.admin.log.modules.logging.vo.LogVO;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>LogService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
@Service
@RequiredArgsConstructor
public class LogService extends MyServiceImpl<LogMapper, LogDO> {
    private final LogMapStruct logSmallMapper;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private QueryWrapper queryWrapper(LogQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (cn.hutool.core.util.StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(username LIKE {0} OR description LIKE {0} OR address LIKE {0} OR request_ip LIKE {0} OR method LIKE {0} OR params LIKE {0} OR detail LIKE {0} OR url LIKE {0} OR module LIKE {0} OR biz_no LIKE {0} OR biz_key LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage<LogVO> queryAll(LogQueryCriteriaDTO criteria, PageParam pageable) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<LogVO> list = logSmallMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 查询条件
     * @return /
     */
    public List<LogVO> queryAll(LogQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        return logSmallMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 查询用户日志
     *
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return -
     */
    public MyPage<LogVO> queryAllByUser(LogQueryCriteriaDTO criteria, PageParam pageable) {
        String username = SecurityUtils.getCurrentUsername();
        criteria.setUsername(username);
        return queryAll(criteria, pageable);
    }

    /**
     * 保存日志数据
     *
     * @param log 日志实体
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void saveLog(LogDO log) {
        log.setAddress(StrUtil.getCityInfo(log.getRequestIp()));
        this.save(log);
    }

    /**
     * 查询异常详情
     *
     * @param id 日志ID
     * @return Object
     */
    public Object findByErrDetail(Long id) {
        LogDO log = this.getById(id);
        ValidationUtil.isNull(log.getId(), "BizLog", "id", id);
        byte[] details = log.getExceptionDetail();
        return Dict.create().set("exception", new String(ObjectUtil.isNotNull(details) ? details : "".getBytes()));
    }

    /**
     * 导出日志
     *
     * @param logs     待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<LogVO> logs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LogVO log : logs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", log.getUsername());
            map.put("IP", log.getRequestIp());
            map.put("IP来源", log.getAddress());
            map.put("描述", log.getDescription());
            map.put("用户代理", log.getUserAgent());
            map.put("浏览器", log.getBrowser());
            map.put("操作系统", log.getOs());
            map.put("请求耗时/毫秒", log.getTime());
            map.put("异常详情", new String(ObjectUtil.isNotNull(log.getExceptionDetail()) ? log.getExceptionDetail() : "".getBytes()));
            map.put("创建日期", log.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 删除所有日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void delAll() {
        //删除6个月之前的数据
        DateTime dateTime = DateUtil.offsetMonth(DateUtil.date(), -6);
        LambdaQueryWrapper<LogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.le(LogDO::getGmtCreate, dateTime);
        this.remove(queryWrapper);
    }

    /**
     * <p>
     * 查询所有日志模块
     * </p>
     *
     * @return 日志模块
     */
    public List<String> listModule() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.isNotNull("module");
        queryWrapper.ne("module", "");
        queryWrapper.select("DISTINCT module");
        queryWrapper.orderByAsc("module");
        return this.listObjs(queryWrapper, a -> {
            return a.toString();
        });
    }

    /**
     * <p>
     * 查询业务标识与业务主键
     * </p>
     *
     * @param bizKey 业务标识
     * @param bizNo  业务主键
     * @return 日志
     * @param page a {@link com.boot.admin.mybatis.param.PageParam} object.
     */
    public MyPage<LogDO> listByBizKeyAndBizNo(PageParam page, String bizKey, String bizNo) {
        LambdaQueryWrapper<LogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogDO::getBizKey, bizKey);
        queryWrapper.eq(LogDO::getIsSuccess, Boolean.TRUE);
        queryWrapper.eq(LogDO::getCategory, LogCategoryType.OPERATING);
        queryWrapper.in(LogDO::getLogType, CollUtil.newHashSet(LogType.ADD, LogType.UPDATE, LogType.DELETE));
        queryWrapper.apply("FIND_IN_SET({0}, biz_no)", bizNo);
        queryWrapper.orderByDesc(LogDO::getGmtCreate);
        return this.page(page, queryWrapper);
    }

    /**
     * <p>
     * 获取登录pv
     * </p>
     *
     * @return /
     */
    public Long getTotalLoginPv() {
        LambdaQueryWrapper<LogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogDO::getModule, LogModule.LOG_MODULE_LOGIN);
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * 获取今天登录pv
     * </p>
     *
     * @return /
     */
    public Long getTodayLoginPv() {
        LambdaQueryWrapper<LogDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LogDO::getModule, LogModule.LOG_MODULE_LOGIN);
        queryWrapper.apply("to_days(gmt_create) = to_days(now())");
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * 获取登录iv
     * </p>
     *
     * @return /
     */
    public Long getTotalLoginIv() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("module", LogModule.LOG_MODULE_LOGIN);
        queryWrapper.select("distinct(request_ip)");
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * 获取今天登录iv
     * </p>
     *
     * @return /
     */
    public Long getTodayLoginIv() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("module", LogModule.LOG_MODULE_LOGIN);
        queryWrapper.select("distinct(request_ip)");
        queryWrapper.apply("to_days(gmt_create) = to_days(now())");
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * 查询十天内的登录统计
     * </p>
     *
     * @param username 用户名
     * @return /
     */
    public List<DashboardChartLastTenVisitVO> findLastTenDaysVisitCount(String username) {
        String tenDaysAgoStr = DateUtil.formatDate(DateUtil.offsetDay(DateUtil.date(), -9));
        return this.baseMapper.findLastTenDaysVisitCount(tenDaysAgoStr, username);
    }

    /**
     * <p>
     * 统计浏览器
     * </p>
     *
     * @return /
     */
    public List<DashboardChartBrowserVO> findByBrowser() {
        return this.baseMapper.findByBrowser();
    }

    /**
     * <p>
     * 统计操作系统
     * </p>
     *
     * @return /
     */
    public List<DashboardChartOperatingSystemVO> findByOs() {
        return this.baseMapper.findByOs();
    }
}
