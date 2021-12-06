package com.boot.admin.system.modules.mnt.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.util.ValidationUtil;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.system.modules.mnt.domain.ServerDeployDO;
import com.boot.admin.system.modules.mnt.dto.ServerDeployDTO;
import com.boot.admin.system.modules.mnt.dto.ServerDeployQueryCriteriaDTO;
import com.boot.admin.system.modules.mnt.mapper.ServerDeployMapper;
import com.boot.admin.system.modules.mnt.mapstruct.ServerDeployMapStruct;
import com.boot.admin.system.modules.mnt.util.ExecuteShellUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>ServerDeployService class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Service
@RequiredArgsConstructor
public class ServerDeployService extends MyServiceImpl<ServerDeployMapper, ServerDeployDO> {

    private final ServerDeployMapStruct serverDeployMapper;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private QueryWrapper queryWrapper(ServerDeployQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR ip LIKE {0} OR account LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(ServerDeployQueryCriteriaDTO criteria, PageParam pageable) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<ServerDeployDTO> list = serverDeployMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<ServerDeployDTO> queryAll(ServerDeployQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public ServerDeployDTO findById(Long id) {
        ServerDeployDO server = this.getById(id);
        ValidationUtil.isNull(server.getId(), "ServerDeployDO", "id", id);
        return serverDeployMapper.toVO(server);
    }

    /**
     * 根据IP查询
     *
     * @param ip /
     * @return /
     */
    public ServerDeployDTO findByIp(String ip) {
        LambdaQueryWrapper<ServerDeployDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServerDeployDO::getIp, ip);
        return serverDeployMapper.toVO(this.getOne(queryWrapper));
    }

    /**
     * 测试登录服务器
     *
     * @param resources /
     * @return /
     */
    public Boolean testConnect(ServerDeployDO resources) {
        ExecuteShellUtil executeShellUtil = null;
        try {
            executeShellUtil = new ExecuteShellUtil(resources.getIp(), resources.getAccount(), resources.getPassword(), resources.getPort());
            return executeShellUtil.execute("ls") == 0;
        } catch (Exception e) {
            return false;
        } finally {
            if (executeShellUtil != null) {
                executeShellUtil.close();
            }
        }
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(ServerDeployDO resources) {
        this.save(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(ServerDeployDO resources) {
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
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<ServerDeployDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ServerDeployDTO deployDto : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("服务器名称", deployDto.getName());
            map.put("服务器IP", deployDto.getIp());
            map.put("端口", deployDto.getPort());
            map.put("账号", deployDto.getAccount());
            map.put("创建日期", deployDto.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
