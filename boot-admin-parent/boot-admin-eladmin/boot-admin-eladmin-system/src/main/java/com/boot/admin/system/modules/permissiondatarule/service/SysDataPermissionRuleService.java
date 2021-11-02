package com.boot.admin.system.modules.permissiondatarule.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.common.util.FileUtil;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleDTO;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleQueryCriteriaDTO;
import com.boot.admin.system.modules.permissiondatarule.api.dto.SysDataPermissionRuleRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.permissiondatarule.api.enums.SysDataPermissionRuleSortEnum;
import com.boot.admin.system.modules.permissiondatarule.api.vo.SysDataPermissionRuleVO;
import com.boot.admin.system.modules.permissiondatarule.domain.SysDataPermissionRuleDO;
import com.boot.admin.system.modules.permissiondatarule.mapper.SysDataPermissionRuleMapper;
import com.boot.admin.system.modules.permissiondatarule.mapstruct.SysDataPermissionRuleMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 数据规则 服务实现类
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-27
 */
@Service
@RequiredArgsConstructor
public class SysDataPermissionRuleService extends MyServiceImpl<SysDataPermissionRuleMapper, SysDataPermissionRuleDO> {

    private final SysDataPermissionRuleMapStruct sysDataPermissionRuleMapStruct;
    private final SysDataPermissionRuleRoleService sysDataPermissionRuleRoleService;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(SysDataPermissionRuleDTO dto) {

        SysDataPermissionRuleDO sysDataPermissionRuleDO = this.sysDataPermissionRuleMapStruct.toDO(dto);
        Assert.isTrue(this.save(sysDataPermissionRuleDO), "保存失败");
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        Assert.isTrue(this.removeBatchByIdsWithFill(new SysDataPermissionRuleDO(), ids) == ids.size(), "删除失败，记录不存在或权限不足");
        sysDataPermissionRuleRoleService.deleteByPermissionRuleIds(ids);
    }

    /**
     * <p>
     * 编辑
     * </p>
     *
     * @param dto 编辑对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDataPermissionRuleDTO dto) {
        SysDataPermissionRuleDO sysDataPermissionRuleDO = this.getById(dto.getId());
        Assert.notNull(sysDataPermissionRuleDO, "记录不存在");


        sysDataPermissionRuleDO = this.sysDataPermissionRuleMapStruct.toDO(dto);
        Assert.isTrue(this.updateById(sysDataPermissionRuleDO), "修改失败，记录不存在或权限不足");
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return SysDataPermissionRuleVO 对象
     */
    public SysDataPermissionRuleVO getVoById(Long id) {
        SysDataPermissionRuleDO sysDataPermissionRuleDO = this.getById(id);
        Assert.notNull(sysDataPermissionRuleDO, "记录不存在或权限不足");
        return this.sysDataPermissionRuleMapStruct.toVO(sysDataPermissionRuleDO);
    }

    /**
     * <p>
     * 查询数据分页
     * </p>
     *
     * @param query 条件
     * @param sort  排序
     * @param page  分页
     * @return SysDataPermissionRuleVO 分页
     */
    public MyPage<SysDataPermissionRuleVO> pageQuery(PageParam page, SysDataPermissionRuleSortEnum sort, SysDataPermissionRuleQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        return this.baseMapper.pageQuery(page, sort, queryWrapper);
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param sort  排序
     * @param query 条件
     * @return SysDataPermissionRuleVO 列表对象
     */
    public List<SysDataPermissionRuleVO> listQueryAll(SysDataPermissionRuleSortEnum sort, SysDataPermissionRuleQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        List<SysDataPermissionRuleDO> list = this.baseMapper.queryAll(sort, queryWrapper);
        return sysDataPermissionRuleMapStruct.toVO(list);
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @param sort  排序
     * @param query 条件
     */
    public void download(SysDataPermissionRuleSortEnum sort, SysDataPermissionRuleQueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<SysDataPermissionRuleVO> all = this.listQueryAll(sort, query);
        for (SysDataPermissionRuleVO sysDataPermissionRule : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单ID", sysDataPermissionRule.getMenuId());
            map.put("名称", sysDataPermissionRule.getName());
            map.put("条件", sysDataPermissionRule.getCondition());
            map.put("列名", sysDataPermissionRule.getColumn());
            map.put("规则值", sysDataPermissionRule.getValue());
            map.put("是否有效 1是 0否", sysDataPermissionRule.getIsActivated());
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        } catch (IOException e) {
            throw new IllegalArgumentException("文件下载失败");
        }
    }

    /**
     * <p>
     * 根据角色id和权限标识查询
     * </p>
     *
     * @param roleIds    角色id
     * @param permission 权限标识
     * @return /
     */
    public List<PermissionDataRuleDTO> listByRoleIdsAndPermission(Set<Long> roleIds, String permission) {
        if (CollUtil.isNotEmpty(roleIds)) {
            return this.getBaseMapper().selectByRoleIdsAndPermission(roleIds, permission);
        }
        return null;
    }

    /**
     * <p>
     * 根据角色id和权限标识查询
     * </p>
     *
     * @param menuId 菜单id
     * @param roleId 角色id
     * @return /
     */
    public MyPage<SysDataPermissionRuleVO> pageByMenuIdAndRoleId(PageParam page, SysDataPermissionRuleRoleQueryCriteriaDTO query) {
        return this.getBaseMapper().pageByMenuIdAndRoleId(page, query.getMenuId(), query.getRoleId());
    }
}