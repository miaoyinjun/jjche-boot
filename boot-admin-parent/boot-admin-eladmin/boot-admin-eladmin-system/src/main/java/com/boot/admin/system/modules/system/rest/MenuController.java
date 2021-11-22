package com.boot.admin.system.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.boot.admin.system.modules.system.domain.MenuDO;
import com.boot.admin.system.modules.system.api.dto.MenuDTO;
import com.boot.admin.system.modules.system.api.dto.MenuQueryCriteriaDTO;
import com.boot.admin.system.modules.system.mapstruct.MenuMapStruct;
import com.boot.admin.system.modules.system.service.MenuService;
import com.boot.admin.system.modules.system.api.vo.MenuVO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.AdminRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>MenuController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */

@RequiredArgsConstructor
@Api(tags = "系统：菜单管理")
@AdminRestController("menus")
public class MenuController extends BaseController {

    private final MenuService menuService;
    private final MenuMapStruct menuMapStruct;
    private static final String ENTITY_NAME = "menu";

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link com.boot.admin.system.modules.system.api.dto.MenuQueryCriteriaDTO} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "菜单"
    )
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteriaDTO criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    /**
     * <p>buildMenus.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public ResultWrapper<List<MenuVO>> buildMenus() {
        List<MenuDTO> menuDtoList = menuService.findByUser(SecurityUtils.getCurrentUserId());
        List<MenuDTO> menuDtos = menuService.buildTree(menuDtoList);
        return ResultWrapper.ok(menuService.buildMenus(menuDtos));
    }

    /**
     * <p>
     * 返回全部的菜单
     * </p>
     *
     * @param pid    父id
     * @param roleId 角色id
     * @return 菜单
     * @author miaoyj
     * @since 2020-12-09
     */
    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResultWrapper<List<MenuDTO>> query(@RequestParam Long pid,
                                              @RequestParam Long roleId) {
        return ResultWrapper.ok(menuService.getMenus(pid, roleId));
    }

    /**
     * <p>child.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @ApiOperation("根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResultWrapper<Set<Long>> child(@RequestParam Long id) {
        Set<MenuDO> menuSet = new HashSet<>();
        List<MenuDTO> menuList = menuService.getMenus(id, null);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapStruct.toDO(menuList), menuSet);
        Set<Long> ids = menuSet.stream().map(MenuDO::getId).collect(Collectors.toSet());
        return ResultWrapper.ok(ids);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.system.api.dto.MenuQueryCriteriaDTO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "菜单"
    )
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public ResultWrapper<MyPage<MenuDTO>> query(MenuQueryCriteriaDTO criteria) throws Exception {
        List<MenuDTO> menuDtoList = menuService.queryAll(criteria, true);
        MyPage<MenuDTO> myPage = new MyPage<>();
        myPage.setRecords(menuDtoList);
        myPage.setTotal(menuDtoList.size());
        return ResultWrapper.ok(myPage);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link com.boot.admin.system.modules.system.api.dto.MenuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询分页", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "菜单"
    )
    @ApiOperation("查询菜单分页")
    @GetMapping("/page")
    @PreAuthorize("@el.check('menu:list')")
    public ResultWrapper<MyPage<MenuDTO>> query(MenuQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(menuService.queryPage(criteria, pageable));
    }

    /**
     * <p>getSuperior.</p>
     *
     * @param ids a {@link java.util.List} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "菜单"
    )
    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public ResultWrapper<List<MenuDTO>> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDTO> menuDtos = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDTO menuDto = menuService.findById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return ResultWrapper.ok(menuService.buildTree(new ArrayList<>(menuDtos)));
        }
        return ResultWrapper.ok(menuService.getMenus(null, null));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.MenuDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "菜单"
    )
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public ResultWrapper create(@Validated @RequestBody MenuDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        menuService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link com.boot.admin.system.modules.system.domain.MenuDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "菜单"
    )
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public ResultWrapper update(@Validated @RequestBody MenuDO resources) {
        menuService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "菜单"
    )
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        Set<MenuDO> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDTO> menuList = menuService.getMenus(id, null);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getChildMenus(menuMapStruct.toDO(menuList), menuSet);
        }
        menuService.delete(menuSet);
        return ResultWrapper.ok();
    }
}
