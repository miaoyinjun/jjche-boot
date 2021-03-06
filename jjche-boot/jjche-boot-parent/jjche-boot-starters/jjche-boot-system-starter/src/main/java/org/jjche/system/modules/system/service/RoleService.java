package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.dto.SimpleGrantedAuthorityDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.DataScopeEnum;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.system.api.dto.RoleDTO;
import org.jjche.system.modules.system.api.dto.RoleQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.*;
import org.jjche.system.modules.system.mapper.RoleDeptMapper;
import org.jjche.system.modules.system.mapper.RoleMapper;
import org.jjche.system.modules.system.mapper.RoleMenuMapper;
import org.jjche.system.modules.system.mapper.UserMapper;
import org.jjche.system.modules.system.mapstruct.RoleMapStruct;
import org.jjche.system.modules.system.mapstruct.RoleSmallMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>RoleService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */
@Service
@RequiredArgsConstructor
public class RoleService extends MyServiceImpl<RoleMapper, RoleDO> {

    private final RoleMenuMapper roleMenuMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final RoleMapStruct roleMapper;
    private final RoleSmallMapStruct roleSmallMapper;
    private final RedisService redisService;
    private final UserMapper userMapper;
    private final JwtUserService jwtUserService;
    private final DataPermissionFieldService dataPermissionFieldService;
    private final DataPermissionRuleService dataPermissionRuleService;

    /**
     * ??????????????????
     *
     * @return /
     */
    public List<RoleDTO> queryAll() {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(RoleDO::getLevel);
        return roleMapper.toVO(this.list(queryWrapper));
    }

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param criteria ??????
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(RoleQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<RoleDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR description LIKE {0})", "%" + blurry + "%");
        }
        queryWrapper.orderByAsc(RoleDO::getLevel);
        return queryWrapper;
    }

    /**
     * ????????????
     *
     * @param criteria ??????
     * @return /
     */
    public List<RoleDTO> queryAll(RoleQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return roleMapper.toVO(this.list(queryWrapper));
    }

    /**
     * ?????????????????????
     *
     * @param criteria ??????
     * @param page     ????????????
     * @return /
     */
    public MyPage<RoleDTO> queryAll(RoleQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage<RoleDO> myPage = this.baseMapper.pageQuery(page, queryWrapper);
        List<RoleDO> roleList = myPage.getRecords();
        if (CollUtil.isNotEmpty(roleList)) {
            for (RoleDO role : roleList) {
                Set<MenuDO> menus = role.getMenus();
                Set<MenuDO> newMenus = CollUtil.newHashSet();
                if (CollUtil.isNotEmpty(menus)) {
                    for (MenuDO menu : menus) {
                        Long menuId = menu.getId();
                        Long countFiled = dataPermissionFieldService.countByMenuId(menuId);
                        Long countRule = dataPermissionRuleService.countByMenuId(menuId);
                        Boolean isDataPermission = (countFiled != null && countFiled > 0) || countRule != null && countRule > 0;
                        menu.setIsDataPermission(isDataPermission);
                        newMenus.add(menu);
                    }
                }
                role.setMenus(newMenus);
            }
        }
        List<RoleDTO> list = roleMapper.toVO(roleList);
        for (RoleDTO roleDTO : list) {
            DataScopeEnum dataScope = roleDTO.getDataScope();
            if (dataScope != null) {
                roleDTO.setDataScopeValue(dataScope.getValue());
            }
        }
        MyPage<RoleDTO> resultPage = new MyPage<>();
        resultPage.setRecords(list);
        resultPage.setPages(myPage.getPages());
        resultPage.setTotal(myPage.getTotal());
        return resultPage;
    }

    /**
     * ??????ID??????
     *
     * @param id /
     * @return /
     */
    @Cached(name = CacheKey.ROLE_ID, key = "#id")
    public RoleDTO findById(long id) {
        RoleDO role = this.getById(id);
        ValidationUtil.isNull(role.getId(), "RoleDO", "id", id);
        return roleMapper.toVO(role);
    }

    /** {@inheritDoc} */
    /**
     * ??????
     *
     * @param resources /
     */
    public void create(RoleDO resources) {
        RoleDO role = this.findByName(resources.getName());
        Assert.isNull(role, StrUtil.format("?????????{}?????????", resources.getName()));
        role = this.findByCode(resources.getCode());
        Assert.isNull(role, StrUtil.format("?????????{}?????????", resources.getCode()));
        this.save(resources);
        List<Long> deptIds = resources.getDepts().stream().map(DeptDO::getId).collect(Collectors.toList());
        this.updateRoleAndDept(resources.getId(), deptIds);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param name ??????
     * @return /
     */
    public RoleDO findByName(String name) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDO::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param code ??????
     * @return /
     */
    public RoleDO findByCode(String code) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDO::getCode, code);
        return this.getOne(queryWrapper);
    }

    /**
     * ??????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDO resources) {
        RoleDO role = this.getById(resources.getId());
        ValidationUtil.isNull(role.getId(), "RoleDO", "id", resources.getId());

        RoleDO role1 = this.findByName(resources.getName());
        Boolean isExist = role1 != null && !role1.getId().equals(role.getId());
        Assert.isFalse(isExist, resources.getName() + "?????????");

        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setLevel(resources.getLevel());
        this.updateById(role);
        List<Long> deptIds = resources.getDepts().stream().map(DeptDO::getId).collect(Collectors.toList());
        updateRoleAndDept(role.getId(), deptIds);
        // ??????????????????
        delCaches(role.getId());
    }

    /**
     * ?????????????????????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(RoleDO resources) {
        Long roleId = resources.getId();
        // ????????????
        List<Long> menuIds = resources.getMenus().stream().map(MenuDO::getId).collect(Collectors.toList());
        updateRoleAndMenu(roleId, menuIds);
        delCaches(resources.getId());
    }

    /**
     * ????????????
     *
     * @param menuId /
     */
    public void untiedMenu(Long menuId) {
        this.baseMapper.untiedMenu(menuId);
    }

    /**
     * ??????
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // ??????????????????
            delCaches(id);
        }
        this.removeByIds(ids);
    }

    /**
     * ????????????ID??????
     *
     * @param id ??????ID
     * @return /
     */
    public List<RoleSmallDto> findByUsersId(Long id) {
        return roleSmallMapper.toVO(this.baseMapper.selectByUserId(id));
    }

    /**
     * ????????????????????????
     *
     * @param user ????????????
     * @return ????????????
     */
    public List<SimpleGrantedAuthorityDTO> mapToGrantedAuthorities(UserVO user) {
        Set<String> permissions = new HashSet<>();
        // ??????????????????????????????
        if (user.getIsAdmin()) {
            permissions.add(SecurityConstant.ADMIN_PERMISSION);
            return permissions.stream().map(SimpleGrantedAuthorityDTO::new)
                    .collect(Collectors.toList());
        }
        List<RoleDO> roles = this.baseMapper.selectByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream())
                .filter(menu -> StrUtil.isNotBlank(menu.getPermission()))
                .map(MenuDO::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthorityDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * ????????????
     *
     * @param roles    ??????????????????
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<RoleDTO> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDTO role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("????????????", role.getName());
            map.put("????????????", role.getLevel());
            map.put("??????", role.getDescription());
            map.put("????????????", role.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * ???????????????????????????
     *
     * @param ids /
     */
    public void verification(Set<Long> ids) {
        Assert.isFalse(userMapper.countByRoles(ids) > 0, "?????????????????????????????????????????????????????????");
    }

    /**
     * ????????????Id??????
     *
     * @param menuIds /
     * @return /
     */
    public List<RoleDO> findInMenuId(List<Long> menuIds) {
        return this.baseMapper.findInMenuId(menuIds);
    }

    /**
     * ????????????
     *
     * @param id /
     */
    public void delCaches(Long id) {
        List<UserDO> users = userMapper.findByRoleId(id);
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> jwtUserService.removeByUserName(item.getUsername()));
        }
        redisService.delete(CacheKey.ROLE_ID + id);
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param roleId  ??????id
     * @param deptIds ??????id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndDept(Long roleId, List<Long> deptIds) {
        LambdaQueryWrapper<RoleDeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDeptDO::getRoleId, roleId);
        this.roleDeptMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(deptIds)) {
            for (Long deptId : deptIds) {
                RoleDeptDO roleDeptDO = new RoleDeptDO(roleId, deptId);
                this.roleDeptMapper.insert(roleDeptDO);
            }
        }
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param roleId  ??????id
     * @param menuIds ??????id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndMenu(Long roleId, List<Long> menuIds) {
        MenuService menuService = SpringUtil.getBean(MenuService.class);
        LambdaQueryWrapper<RoleMenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenuDO::getRoleId, roleId);
        this.roleMenuMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                RoleMenuDO roleMenuDO = new RoleMenuDO(roleId, menuId);
                this.roleMenuMapper.insert(roleMenuDO);
                menuService.delCaches(menuId);
            }
        }
    }

    /**
     * <p>
     * ??????????????????????????????
     * </p>
     *
     * @param roleIds /
     * @return /
     */
    public Integer findByRoles(List<Long> roleIds) {
        if (roleIds.size() == 0) {
            return Integer.MAX_VALUE;
        }
        Set<RoleDTO> roleDtos = new HashSet<>();
        for (Long roleId : roleIds) {
            roleDtos.add(findById(roleId));
        }
        return Collections.min(roleDtos.stream().map(RoleDTO::getLevel).collect(Collectors.toList()));
    }
}
