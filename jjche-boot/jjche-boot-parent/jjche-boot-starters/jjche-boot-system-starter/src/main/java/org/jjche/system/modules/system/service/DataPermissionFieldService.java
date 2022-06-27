package org.jjche.system.modules.system.service;

import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldVO;
import org.jjche.system.modules.system.domain.DataPermissionFieldDO;
import org.jjche.system.modules.system.mapper.DataPermissionFieldMapper;
import org.jjche.system.modules.system.mapstruct.DataPermissionFieldMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据字段服务
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-17
 */
@Service
@RequiredArgsConstructor
public class DataPermissionFieldService extends MyServiceImpl<DataPermissionFieldMapper, DataPermissionFieldDO> {

    private final DataPermissionFieldMapStruct dataPermissionFieldMapper;
    private final DataPermissionFieldRoleService dataPermissionFieldRoleService;

    /**
     * <p>
     * 查询
     * </p>
     *
     * @param pageable 分页
     * @param criteria 条件
     * @return /
     */
    public MyPage pageQuery(PageParam pageable, DataPermissionFieldQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<DataPermissionFieldDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        queryWrapper.orderByAsc(DataPermissionFieldDO::getSort);
        MyPage<DataPermissionFieldDO> myPage = this.page(pageable, queryWrapper);
        List<DataPermissionFieldVO> list = dataPermissionFieldMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * <p>
     * 根据ID查询
     * </p>
     *
     * @param id ID
     * @return DataPermissionFieldVO 对象
     */
    public DataPermissionFieldVO getVoById(Long id) {
        DataPermissionFieldDO dataPermissionFieldDO = this.getById(id);
        Assert.notNull(dataPermissionFieldDO, "记录不存在或权限不足");
        return this.dataPermissionFieldMapper.toVO(dataPermissionFieldDO);
    }

    /**
     * <p>
     * 删除
     * </p>
     *
     * @param ids 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        Assert.isTrue(this.removeBatchByIdsWithFill(new DataPermissionFieldDO(), ids) == ids.size(), "删除失败，记录不存在或权限不足");
        dataPermissionFieldRoleService.deleteByPermissionFieldIds(ids);
        dataPermissionFieldRoleService.delUserCache();
    }

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param resources 内容
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DataPermissionFieldDTO resources) {
        this.save(dataPermissionFieldMapper.toDO(resources));
    }

    /**
     * <p>
     * 修改
     * </p>
     *
     * @param dto 内容
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DataPermissionFieldDTO dto) {
        DataPermissionFieldDO dataPermissionField = this.getById(dto.getId());
        Assert.notNull(dataPermissionField, "记录不存在");

        dataPermissionField = this.dataPermissionFieldMapper.toDO(dto);
        Assert.isTrue(this.updateById(dataPermissionField), "修改失败，记录不存在或权限不足");
        dataPermissionFieldRoleService.delUserCache();
    }

    /**
     * <p>
     * 根据菜单id统计
     * </p>
     *
     * @param menuId 菜单id
     * @return /
     */
    public Long countByMenuId(Long menuId) {
        LambdaQueryWrapper<DataPermissionFieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataPermissionFieldDO::getMenuId, menuId);
        return this.count(queryWrapper);
    }

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    @Cached(name = CacheKey.PERMISSION_DATA_FIELD_USER_ID, key = "#userId")
    public List<DataPermissionFieldVO> listByUserId(Long userId) {
        return this.getBaseMapper().queryByUserId(userId);
    }
}
