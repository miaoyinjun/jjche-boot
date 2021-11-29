package com.boot.admin.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.cache.service.RedisService;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldRoleDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFieldRoleQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.dto.DataPermissionFiledRoleSelectedDTO;
import com.boot.admin.system.modules.system.api.enums.DataPermissionFieldRoleSortEnum;
import com.boot.admin.system.modules.system.api.vo.DataPermissionFieldRoleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldDO;
import com.boot.admin.system.modules.system.domain.DataPermissionFieldRoleDO;
import com.boot.admin.system.modules.system.mapper.DataPermissionFieldRoleMapper;
import com.boot.admin.system.modules.system.mapstruct.DataPermissionFieldRoleMapStruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 数据字段角色 服务实现类
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-04
 * @version 1.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class DataPermissionFieldRoleService extends MyServiceImpl<DataPermissionFieldRoleMapper, DataPermissionFieldRoleDO> {

    private final DataPermissionFieldRoleMapStruct sysDataPermissionFieldRoleMapStruct;
    private final RedisService redisService;

    /**
     * <p>
     * 创建
     * </p>
     *
     * @param dto 创建对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void save(DataPermissionFieldRoleDTO dto) {
        Long roleId = dto.getRoleId();
        Long menuId = dto.getMenuId();
        //删除
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionFieldRoleDO::getRoleId, roleId);
        queryWrapper.in(DataPermissionFieldRoleDO::getMenuId, menuId);
        this.remove(queryWrapper);

        List<DataPermissionFiledRoleSelectedDTO> dataPermissionFieldSelectedList = dto.getDataPermissionFieldSelectedList();
        if (CollUtil.isNotEmpty(dataPermissionFieldSelectedList)) {
            List<DataPermissionFieldRoleDO> sysDataPermissionFieldRoleDOList = CollUtil.newArrayList();
            for (DataPermissionFiledRoleSelectedDTO selectedDTO : dataPermissionFieldSelectedList) {
                DataPermissionFieldRoleDO sysDataPermissionFieldRoleDO = new DataPermissionFieldRoleDO();
                sysDataPermissionFieldRoleDO.setRoleId(roleId);
                sysDataPermissionFieldRoleDO.setMenuId(menuId);
                sysDataPermissionFieldRoleDO.setDataPermissionFieldId(selectedDTO.getId());
                sysDataPermissionFieldRoleDO.setIsAccessible(selectedDTO.getIsAccessible());
                sysDataPermissionFieldRoleDO.setIsEditable(selectedDTO.getIsEditable());
                sysDataPermissionFieldRoleDOList.add(sysDataPermissionFieldRoleDO);
            }
            Assert.isTrue(this.saveBatch(sysDataPermissionFieldRoleDOList), "保存失败");
        }
        this.delUserCache();
    }

    /**
     * <p>
     * 查询所有数据不分页
     * </p>
     *
     * @param sort  排序
     * @param query 条件
     * @return DataPermissionFieldRoleVO 列表对象
     */
    public List<DataPermissionFieldRoleVO> listQueryAll(DataPermissionFieldRoleSortEnum sort, DataPermissionFieldRoleQueryCriteriaDTO query) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(query);
        List<DataPermissionFieldRoleDO> list = this.baseMapper.queryAll(sort, queryWrapper);
        return sysDataPermissionFieldRoleMapStruct.toVO(list);
    }

    /**
     * <p>
     * 多选删除
     * </p>
     *
     * @param permissionRuleIds 数据规则id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByPermissionFieldIds(Set<Long> permissionRuleIds) {
        LambdaQueryWrapper<DataPermissionFieldRoleDO> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(DataPermissionFieldRoleDO::getDataPermissionFieldId, permissionRuleIds);
        this.remove(queryWrapper);
    }

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page  分页
     * @param query 条件
     * @return 分页VO
     */
    public MyPage<DataPermissionFieldRoleVO> page(PageParam page, DataPermissionFieldRoleQueryCriteriaDTO query) {
        DataPermissionFieldService dataPermissionFieldService = SpringUtil.getBean(DataPermissionFieldService.class);
        MyPage<DataPermissionFieldRoleVO> resultPage = new MyPage<>();
        List<DataPermissionFieldRoleVO> resultList = new ArrayList<>();

        //查询字段表
        DataPermissionFieldQueryCriteriaDTO fileQueryCriteria = new DataPermissionFieldQueryCriteriaDTO();
        fileQueryCriteria.setMenuId(query.getMenuId());
        QueryWrapper fileRolequeryWrapper = MybatisUtil.assemblyQueryWrapper(fileQueryCriteria);
        MyPage<DataPermissionFieldDO> filedPage = dataPermissionFieldService.page(page, fileRolequeryWrapper);
        if (filedPage != null) {
            List<DataPermissionFieldDO> filedList = filedPage.getRecords();
            if (CollUtil.isNotEmpty(filedList)) {
                List<DataPermissionFieldRoleVO> list = this.listQueryAll(null, query);

                for (DataPermissionFieldDO fieldDO : filedList) {
                    Long fieldId = fieldDO.getId();
                    String fieldName = fieldDO.getName();
                    Boolean isActivated = fieldDO.getIsActivated();

                    DataPermissionFieldRoleVO fieldRoleVO = new DataPermissionFieldRoleVO();
                    fieldRoleVO.setId(fieldId);
                    fieldRoleVO.setName(fieldName);
                    fieldRoleVO.setIsActivated(isActivated);

                    for (DataPermissionFieldRoleVO fieldRole : list) {
                        if (fieldId.equals(fieldRole.getDataPermissionFieldId())) {
                            fieldRoleVO.setIsAccessible(fieldRole.getIsAccessible());
                            fieldRoleVO.setIsEditable(fieldRole.getIsEditable());
                            fieldRoleVO.setIsSelected(true);
                            break;
                        }
                    }
                    resultList.add(fieldRoleVO);
                }
            }
        }
        resultPage.setNewRecords(resultList);
        resultPage.setPages(filedPage.getPages());
        resultPage.setTotal(filedPage.getTotal());
        return resultPage;
    }


    /**
     * <p>
     * 删除用户数据字段缓存
     * </p>
     */
    public void delUserCache(){
        redisService.delByKeyPrefix(CacheKey.PERMISSION_DATA_FIELD_USER_ID);
    }

}
