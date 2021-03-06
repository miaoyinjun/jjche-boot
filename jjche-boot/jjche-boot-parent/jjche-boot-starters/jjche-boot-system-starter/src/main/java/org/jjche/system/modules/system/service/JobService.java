package org.jjche.system.modules.system.service;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.JobDTO;
import org.jjche.system.modules.system.api.dto.JobQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.JobDO;
import org.jjche.system.modules.system.mapper.JobMapper;
import org.jjche.system.modules.system.mapstruct.JobMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>JobService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
@Service
@RequiredArgsConstructor
public class JobService extends MyServiceImpl<JobMapper, JobDO> {

    private final JobMapStruct jobMapstruct;
    private final UserService userService;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(JobQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<JobDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        queryWrapper.orderByAsc(JobDO::getJobSort);
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public MyPage queryAll(JobQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper<JobDO> queryWrapper = queryWrapper(criteria);
        MyPage<JobDO> myPage = this.page(page, queryWrapper);
        List<JobDTO> list = jobMapstruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    public List<JobDO> queryAll(JobQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }


    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public JobDTO findById(Long id) {
        JobDO job = this.getById(id);
        return jobMapstruct.toVO(job);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(JobDTO resources) {
        JobDO job = getByName(resources.getName());
        Assert.isNull(job, resources.getName() + "已存在");
        this.save(jobMapstruct.toDO(resources));
    }

    /**
     * <p>
     * 根据岗位名查询
     * </p>
     *
     * @param jobName 岗位名
     * @return 岗位
     */
    public JobDO getByName(String jobName) {
        LambdaQueryWrapper<JobDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JobDO::getName, jobName);
        return this.getOne(queryWrapper);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(JobDO resources) {
        JobDO job = this.getById(resources.getId());
        JobDO old = this.getByName(resources.getName());
        Boolean isEqualOld = old != null && !old.getId().equals(resources.getId());
        Assert.isFalse(isEqualOld, resources.getName() + "已存在");
        ValidationUtil.isNull(job.getId(), "JobDO", "id", resources.getId());
        resources.setId(job.getId());
        this.updateById(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param jobDtoList 待导出的数据
     * @param response   /
     * @throws java.io.IOException if any.
     */
    public void download(List<JobDO> jobDtoList, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (JobDO jobDTO : jobDtoList) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", jobDTO.getName());
            map.put("岗位状态", jobDTO.getEnabled() ? "启用" : "停用");
            map.put("创建日期", jobDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    public void verification(Set<Long> ids) {
        Assert.isFalse(userService.countByJobs(ids) > 0, "所选的岗位中存在用户关联，请解除关联再试！");
    }
}
