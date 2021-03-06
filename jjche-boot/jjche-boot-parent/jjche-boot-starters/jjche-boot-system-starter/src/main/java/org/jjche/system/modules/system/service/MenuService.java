package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.MenuDTO;
import org.jjche.system.modules.system.api.dto.MenuQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.MenuMetaVO;
import org.jjche.system.modules.system.api.vo.MenuVO;
import org.jjche.system.modules.system.domain.MenuDO;
import org.jjche.system.modules.system.domain.RoleDO;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.system.mapper.MenuMapper;
import org.jjche.system.modules.system.mapper.UserMapper;
import org.jjche.system.modules.system.mapstruct.MenuMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>MenuService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class MenuService extends MyServiceImpl<MenuMapper, MenuDO> {

    private final UserMapper userRepository;
    private final DataPermissionFieldService dataPermissionFieldService;
    private final DataPermissionRuleService dataPermissionRuleService;
    private final MenuMapStruct menuMapStruct;
    private final RoleService roleService;
    private final RedisService redisService;

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param criteria ??????
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(MenuQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<MenuDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(title LIKE {0} OR component LIKE {0} OR permission LIKE {0})", "%" + blurry + "%");
        }
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return queryWrapper;
    }

    /**
     * ??????????????????
     *
     * @param criteria ??????
     * @param isQuery  ????????????pid
     * @return /
     * @throws java.lang.IllegalAccessException if any.
     */
    public List<MenuDTO> queryAll(MenuQueryCriteriaDTO criteria, Boolean isQuery) throws IllegalAccessException {
        if (isQuery) {
            criteria.setPidIsNull(true);
            Field[] fields = ClassUtil.getDeclaredFields(criteria.getClass());
            for (Field field : fields) {
                //???????????????????????????????????????private??????????????????
                field.setAccessible(true);
                Object val = field.get(criteria);
                if ("pidIsNull".equals(field.getName())) {
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return menuMapStruct.toVO(this.list(queryWrapper));
    }

    /**
     * <p>queryPage.</p>
     *
     * @param criteria a {@link MenuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link PageParam} object.
     */
    public MyPage<MenuDTO> queryPage(MenuQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<MenuDTO> list = menuMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * ??????ID??????
     *
     * @param id /
     * @return /
     */
    @Cached(name = CacheKey.MENU_ID, key = "#id")
    public MenuDTO findById(long id) {
        MenuDO menu = this.getById(id);
        return menuMapStruct.toVO(menu);
    }

    /**
     * ??????????????????????????????
     *
     * @param currentUserId /
     * @return /
     */
    @Cached(name = CacheKey.MENU_USER_ID, key = "#currentUserId")
    public List<MenuDTO> findByUser(Long currentUserId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(MenuDO::getType, 2);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        //?????????????????????
        List<MenuDO> menus = null;
        if (SecurityUtil.isAdmin()) {
            menus = this.list(queryWrapper);
        } else {
            List<RoleSmallDto> roles = roleService.findByUsersId(currentUserId);
            Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
            menus = this.baseMapper.findByRoleIdsAndTypeNot(roleIds, 2);
        }
        return this.menuMapStruct.toVO(menus);
    }

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param permission ????????????
     * @return /
     */
    public MenuDO findByPermission(String permission) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPermission, permission);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param title ??????
     * @return /
     */
    public MenuDO findByTitle(String title) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getTitle, title);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @param name ?????????
     * @return /
     */
    public MenuDO findByName(String name) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ?????????id??????
     * </p>
     *
     * @param pId ???id
     * @return /
     */
    public List<MenuDO> findByPid(Long pId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPid, pId);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return this.list(queryWrapper);
    }

    /**
     * <p>
     * ?????????id???null
     * </p>
     *
     * @return /
     */
    public List<MenuDO> findByPidIsNull() {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(MenuDO::getPid);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return this.list(queryWrapper);
    }

    /**
     * <p>
     * ?????????id
     * </p>
     *
     * @param pId ???id
     * @return /
     */
    public Long countByPid(Long pId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPid, pId);
        return this.count(queryWrapper);
    }

    /**
     * ??????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(MenuDO resources) {
        MenuDO menu = this.findByTitle(resources.getTitle());
        Assert.isNull(menu, resources.getTitle() + "?????????");
        String componentName = resources.getName();
        if (StrUtil.isNotBlank(componentName)) {
            MenuDO menu1 = this.findByName(componentName);
            Assert.isNull(menu1, resources.getName() + "?????????");
        }
        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }
        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            Boolean isHttpOrHttps = !(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https));
            Assert.isFalse(isHttpOrHttps, "???????????????http://??????https://??????");
        }
        this.save(resources);
        // ?????????????????????
        resources.setSubCount(0);
        // ???????????????????????????
        updateSubCnt(resources.getPid());
    }

    /**
     * ??????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDO resources) {
        Boolean isSelf = resources.getId().equals(resources.getPid());
        Assert.isFalse(isSelf, "?????????????????????");
        MenuDO menu = this.getById(resources.getId());
        ValidationUtil.isNull(menu.getId(), "Permission", "id", resources.getId());

        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            Boolean isHttpOrHttps = !(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https));
            Assert.isFalse(isHttpOrHttps, "???????????????http://??????https://??????");
        }
        MenuDO menu1 = this.findByTitle(resources.getTitle());
        Boolean isMenuEqual = menu1 != null && !menu1.getId().equals(menu.getId());
        Assert.isFalse(isMenuEqual, resources.getTitle() + "?????????");

        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }

        // ??????????????????ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();
        String componentName = resources.getName();
        if (StrUtil.isNotBlank(componentName)) {
            menu1 = this.findByName(componentName);
            Boolean isMenuEqual2 = menu1 != null && !menu1.getId().equals(menu.getId());
            Assert.isFalse(isMenuEqual2, componentName + "?????????");
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setName(resources.getName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        this.updateById(menu);
        // ??????????????????????????????
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // ????????????
        delCaches(resources.getId());
    }

    /**
     * ????????????????????????????????????ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    public Set<MenuDO> getChildMenus(List<MenuDO> menuList, Set<MenuDO> menuSet) {
        for (MenuDO menu : menuList) {
            menuSet.add(menu);
            List<MenuDO> menus = this.findByPid(menu.getId());
            if (menus != null && menus.size() != 0) {
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * ??????
     *
     * @param menuSet /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<MenuDO> menuSet) {
        for (MenuDO menu : menuSet) {
            // ????????????
            delCaches(menu.getId());
            roleService.untiedMenu(menu.getId());
            this.removeById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }

    /**
     * <p>
     * ???????????????
     * </p>
     *
     * @param pid    ???id
     * @param roleId ??????id
     * @return /
     */
    public List<MenuDTO> getMenus(Long pid, Long roleId) {
        List<MenuDO> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = this.findByPid(pid);
        } else {
            menus = this.findByPidIsNull();
        }
        List<MenuDTO> list = menuMapStruct.toVO(menus);
        if (roleId != null && roleId > 0 && CollUtil.isNotEmpty(list)) {
            for (MenuDTO menuDto : list) {
                Long menuId = menuDto.getId();
                Long countFiled = dataPermissionFieldService.countByMenuId(menuId);
                Long countRule = dataPermissionRuleService.countByMenuId(menuId);
                Boolean isDataPermission = (countFiled != null && countFiled > 0) || countRule != null && countRule > 0;
                menuDto.setIsDataPermission(isDataPermission);
            }
        }
        return list;
    }

    /**
     * ??????ID???????????????????????????
     *
     * @param menuDto /
     * @param menus   /
     * @return /
     */
    public List<MenuDTO> getSuperior(MenuDTO menuDto, List<MenuDO> menus) {
        if (menuDto.getPid() == null) {
            menus.addAll(this.findByPidIsNull());
            return menuMapStruct.toVO(menus);
        }
        menus.addAll(this.findByPid(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    /**
     * ???????????????
     *
     * @param menuDtos ????????????
     * @return /
     */
    public List<MenuDTO> buildTree(List<MenuDTO> menuDtos) {
        List<MenuDTO> trees = new ArrayList<>();
        Set<Long> ids = new HashSet<>();
        for (MenuDTO menuDTO : menuDtos) {
            if (menuDTO.getPid() == null) {
                trees.add(menuDTO);
            }
            for (MenuDTO it : menuDtos) {
                if (menuDTO.getId().equals(it.getPid())) {
                    if (menuDTO.getChildren() == null) {
                        menuDTO.setChildren(new ArrayList<>());
                    }
                    menuDTO.getChildren().add(it);
                    ids.add(it.getId());
                }
            }
        }
        if (trees.size() == 0) {
            trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
        }
        return trees;
    }

    /**
     * ???????????????
     *
     * @param menuDtos /
     * @return /
     */
    public List<MenuVO> buildMenus(List<MenuDTO> menuDtos) {
        List<MenuVO> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDTO> menuDtoList = menuDTO.getChildren();
                        MenuVO menuVo = new MenuVO();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // ????????????????????????????????????????????????
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // ??????????????????
                        if (!menuDTO.getIFrame()) {
                            if (menuDTO.getPid() == null) {
                                menuVo.setComponent(StrUtil.isEmpty(menuDTO.getComponent()) ? "Layout" : menuDTO.getComponent());
                            } else if (!StrUtil.isEmpty(menuDTO.getComponent())) {
                                menuVo.setComponent(menuDTO.getComponent());
                            }
                        }
                        menuVo.setMeta(new MenuMetaVO(menuDTO.getTitle(), menuDTO.getIcon(), !menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // ???????????????????????????????????????????????????
                        } else if (menuDTO.getPid() == null) {
                            MenuVO menuVo1 = new MenuVO();
                            menuVo1.setMeta(menuVo.getMeta());
                            // ?????????
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * ??????ID??????
     *
     * @param id /
     * @return /
     */
    public MenuDO findOne(Long id) {
        MenuDO menu = this.getById(id);
        ValidationUtil.isNull(menu.getId(), "MenuDO", "id", id);
        return menu;
    }

    /**
     * ??????
     *
     * @param menuDtos ??????????????????
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<MenuDTO> menuDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : menuDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("????????????", menuDTO.getTitle());
            map.put("????????????", NumberUtil.equals(menuDTO.getType(), 0) ? "??????" : NumberUtil.equals(menuDTO.getType(), 1) ? "??????" : "??????");
            map.put("????????????", menuDTO.getPermission());
            map.put("????????????", menuDTO.getIFrame() ? "???" : "???");
            map.put("????????????", menuDTO.getHidden() ? "???" : "???");
            map.put("????????????", menuDTO.getCache() ? "???" : "???");
            map.put("????????????", menuDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param menuId ??????id
     */
    private void updateSubCnt(Long menuId) {
        if (menuId != null) {
            Long count = this.countByPid(menuId);
            UpdateWrapper updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("sub_count", count);
            updateWrapper.eq("id", menuId);
            this.update(updateWrapper);
        }
    }

    /**
     * ????????????
     *
     * @param id ??????ID
     */
    public void delCaches(Long id) {
        List<UserDO> users = userRepository.findByMenuId(id);
        redisService.delete(CacheKey.MENU_ID + id);
        redisService.delByKeys(CacheKey.MENU_USER_ID, users.stream().map(UserDO::getId).collect(Collectors.toSet()));
        // ?????? RoleDO ??????
        List<RoleDO> roles = roleService.findInMenuId(new ArrayList<Long>() {{
            add(id);
        }});
        redisService.delByKeys(CacheKey.ROLE_ID, roles.stream().map(RoleDO::getId).collect(Collectors.toSet()));
    }
}
