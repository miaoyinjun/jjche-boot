package com.boot.admin.system.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.util.ValidationUtil;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldDO;
import com.boot.admin.system.modules.system.dto.DataPermissionFieldDTO;
import com.boot.admin.system.modules.system.dto.DataPermissionFieldQueryCriteriaDTO;
import com.boot.admin.system.modules.system.mapper.DataPermissionFieldMapper;
import com.boot.admin.system.modules.system.mapstruct.DataPermissionFieldMapStruct;
import com.boot.admin.system.modules.system.vo.DataPermissionFieldVO;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据字段权限服务
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

    /**
     * <p>
     * 查询
     * </p>
     *
     * @param criteria 条件
     * @param pageable 分页
     * @return /
     */
    public MyPage queryAll(DataPermissionFieldQueryCriteriaDTO criteria, PageParam pageable) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        queryWrapper.orderByAsc("sort");
        MyPage<DataPermissionFieldDO> myPage = this.page(pageable, queryWrapper);
        List<DataPermissionFieldVO> list = dataPermissionFieldMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * <p>
     * 删除
     * </p>
     *
     * @param id 主键
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.removeById(id);
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
     * @param resources 内容
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DataPermissionFieldDTO resources) {
        Long id = resources.getId();
        DataPermissionFieldDO dataPermissionField = this.getById(id);
        ValidationUtil.isNull(id, "DataPermissionFieldDO", "id", resources.getId());
        resources.setId(id);
        this.updateById(dataPermissionFieldMapper.toDO(resources));
    }

    /**
     * <p>
     * 根据菜单id查询
     * </p>
     *
     * @param menuId 菜单id
     * @return /
     */
    public List<DataPermissionFieldDO> findByMenuId(Long menuId) {
        LambdaQueryWrapper<DataPermissionFieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataPermissionFieldDO::getMenuId, menuId);
        return this.list(queryWrapper);
    }
}
