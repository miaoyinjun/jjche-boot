package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.enums.DataScopeEnum;
import org.jjche.common.param.MyPage;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.util.StrUtil;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.DeptDTO;
import org.jjche.system.modules.system.api.dto.DeptQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.DeptDO;
import org.jjche.system.modules.system.mapper.DeptMapper;
import org.jjche.system.modules.system.mapper.RoleMapper;
import org.jjche.system.modules.system.mapper.UserMapper;
import org.jjche.system.modules.system.mapstruct.DeptMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>DeptService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@Service
@RequiredArgsConstructor
public class DeptService extends MyServiceImpl<DeptMapper, DeptDO> {

    private final DeptMapStruct deptMapstruct;
    private final UserMapper userRepository;
    private final RedisService redisService;
    private final RoleMapper roleMapper;

    /**
     * ????????????????????????
     *
     * @return ??????
     */
    public static String getDataScopeType() {
        DataScope dataScope = SecurityUtil.getUserDataScope();
        Set<Long> dataScopes = dataScope.getDeptIds();
        if (dataScopes.size() != 0) {
            return "";
        }
        return DataScopeEnum.DATA_SCOPE_ALL.getValue();
    }

    /**
     * ??????????????????
     *
     * @param criteria ??????
     * @param isQuery  /
     * @return /
     * @throws java.lang.Exception if any.
     */
    public List<DeptDTO> queryAll(DeptQueryCriteriaDTO criteria, Boolean isQuery) throws Exception {
        String dataScopeType = getDataScopeType();
        if (isQuery) {
            if (dataScopeType.equals(DataScopeEnum.DATA_SCOPE_ALL.getValue())) {
                criteria.setPidIsNull(true);
            }
            Field[] fields = ClassUtil.getDeclaredFields(criteria.getClass());
            List<String> fieldNames = new ArrayList<String>() {{
                add("pidIsNull");
                add("enabled");
            }};
            for (Field field : fields) {
                //???????????????????????????????????????private??????????????????
                field.setAccessible(true);
                Object val = field.get(criteria);
                if (fieldNames.contains(field.getName())) {
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }

        LambdaQueryWrapper<DeptDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        queryWrapper.orderByAsc(DeptDO::getDeptSort);
        List<DeptDO> doList = this.list(queryWrapper);
        List<DeptDTO> list = deptMapstruct.toVO(doList);
        // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (StrUtil.isBlank(dataScopeType)) {
            return deduplication(list);
        }
        return list;
    }

    /**
     * ??????ID??????
     *
     * @param id /
     * @return /
     */
    @Cached(name = CacheKey.DEPT_ID, key = "#id")
    public DeptDTO findById(Long id) {
        DeptDO dept = this.getById(id);
        Assert.notNull(dept, "???????????????");
        return deptMapstruct.toVO(dept);
    }

    /**
     * ??????PID??????
     *
     * @param pid /
     * @return /
     */
    public List<DeptDO> findByPid(long pid) {
        LambdaQueryWrapper<DeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptDO::getPid, pid);
        return this.list(queryWrapper);
    }

    /**
     * ????????????ID??????
     *
     * @param id /
     * @return /
     */
    public List<DeptDO> findByRoleId(Long id) {
        return this.baseMapper.selectByRoleId(id);
    }

    /**
     * ??????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DeptDO resources) {
        this.save(resources);
        // ?????????????????????
        resources.setSubCount(0);
        // ????????????
        updateSubCnt(resources.getPid());
    }

    /**
     * ??????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DeptDO resources) {
        // ????????????
        Long oldPid = findById(resources.getId()).getPid();
        Long newPid = resources.getPid();
        Boolean isSelf = resources.getPid() != null && resources.getId().equals(resources.getPid());
        Assert.isFalse(isSelf, "?????????????????????");
        DeptDO dept = this.getById(resources.getId());
        ValidationUtil.isNull(dept.getId(), "DeptDO", "id", resources.getId());
        resources.setId(dept.getId());
        this.updateById(resources);
        // ?????????????????????????????????
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // ????????????
        delCaches(resources.getId());
    }

    /**
     * ??????
     *
     * @param deptDtos /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<DeptDTO> deptDtos) {
        for (DeptDTO deptDto : deptDtos) {
            // ????????????
            delCaches(deptDto.getId());
            this.removeById(deptDto.getId());
            updateSubCnt(deptDto.getPid());
        }
    }

    /**
     * ????????????
     *
     * @param deptDtos ??????????????????
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<DeptDTO> deptDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DeptDTO deptDTO : deptDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("????????????", deptDTO.getName());
            map.put("????????????", deptDTO.getEnabled() ? "??????" : "??????");
            map.put("????????????", deptDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * ????????????????????????
     *
     * @param deptList /
     * @param deptDtos /
     * @return /
     */
    public Set<DeptDTO> getDeleteDepts(List<DeptDO> deptList, Set<DeptDTO> deptDtos) {
        for (DeptDO dept : deptList) {
            deptDtos.add(deptMapstruct.toVO(dept));
            List<DeptDO> depts = this.findByPid(dept.getId());
            if (depts != null && depts.size() != 0) {
                getDeleteDepts(depts, deptDtos);
            }
        }
        return deptDtos;
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @param deptList ??????
     * @return ??????
     */
    public List<Long> getDeptChildren(List<DeptDO> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept != null && dept.getEnabled()) {
                        List<DeptDO> depts = this.findByPid(dept.getId());
                        if (deptList.size() != 0) {
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }

    /**
     * ??????ID???????????????????????????
     *
     * @param deptDto /
     * @param depts   /
     * @return /
     */
    public List<DeptDTO> getSuperior(DeptDTO deptDto, List<DeptDO> depts) {
        if (deptDto.getPid() == null) {
            depts.addAll(this.findByPidIsNull());
            return deptMapstruct.toVO(depts);
        }
        depts.addAll(this.findByPid(deptDto.getPid()));
        return getSuperior(this.findById(deptDto.getPid()), depts);
    }

    private List<DeptDO> findByPidIsNull() {
        LambdaQueryWrapper<DeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(DeptDO::getPid);
        return this.list(queryWrapper);
    }

    private Long countByPid(Long deptId) {
        LambdaQueryWrapper<DeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptDO::getPid, deptId);
        return this.count(queryWrapper);
    }

    /**
     * ??????????????????
     *
     * @param deptDtos /
     * @return /
     */
    public MyPage<List<DeptDTO>> buildTree(List<DeptDTO> deptDtos) {
        Set<DeptDTO> trees = new LinkedHashSet<>();
        Set<DeptDTO> depts = new LinkedHashSet<>();
        List<String> deptNames = deptDtos.stream().map(DeptDTO::getName).collect(Collectors.toList());
        boolean isChild;
        for (DeptDTO deptDTO : deptDtos) {
            isChild = false;
            if (deptDTO.getPid() == null) {
                trees.add(deptDTO);
            }
            for (DeptDTO it : deptDtos) {
                if (it.getPid() != null && deptDTO.getId().equals(it.getPid())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                depts.add(deptDTO);
            } else if (deptDTO.getPid() != null && !deptNames.contains(findById(deptDTO.getPid()).getName())) {
                depts.add(deptDTO);
            }
        }

        if (CollectionUtil.isEmpty(trees)) {
            trees = depts;
        }
        MyPage myPage = new MyPage();
        List<DeptDTO> list = CollectionUtil.isEmpty(trees) ? deptDtos : CollUtil.newArrayList(trees);
        myPage.setRecords(list);
        myPage.setTotal(list.size());
        return myPage;
    }

    /**
     * ????????????????????????????????????
     *
     * @param deptDtos /
     */
    public void verification(Set<DeptDTO> deptDtos) {
        Set<Long> deptIds = deptDtos.stream().map(DeptDTO::getId).collect(Collectors.toSet());
        Assert.isFalse(userRepository.countByDepts(deptIds) > 0, "??????????????????????????????????????????????????????");
        Assert.isFalse(roleMapper.countByDepts(deptIds) > 0, "??????????????????????????????????????????????????????");
    }

    private void updateSubCnt(Long deptId) {
        if (deptId != null) {
            Long count = this.countByPid(deptId);
            this.updateSubCntById(count, deptId);
        }
    }

    private List<DeptDTO> deduplication(List<DeptDTO> list) {
        List<DeptDTO> deptDtos = new ArrayList<>();
        for (DeptDTO deptDto : list) {
            boolean flag = true;
            for (DeptDTO dto : list) {
                if (dto.getId().equals(deptDto.getPid())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                deptDtos.add(deptDto);
            }
        }
        return deptDtos;
    }

    /**
     * ????????????
     *
     * @param id /
     */
    public void delCaches(Long id) {
        // ??????????????????
        redisService.delete(CacheKey.DEPT_ID + id);
    }

    /**
     * <p>
     * ???????????????id
     * </p>
     *
     * @param deptId ??????id
     * @return ?????????id
     * @author miaoyj
     * @since 2020-11-05
     */
    public List<Long> getDeptChildrenIds(Long deptId) {
        return this.findByPid(deptId).stream().map(DeptDO::getId)
                .collect(Collectors.toList());
    }

    /**
     * ??????ID??????sub_count
     *
     * @param count /
     * @param id    /
     */
    public void updateSubCntById(Long count, Long id) {
        LambdaUpdateWrapper<DeptDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DeptDO::getSubCount, count);
        updateWrapper.eq(DeptDO::getId, id);
        this.update(updateWrapper);
    }
}
