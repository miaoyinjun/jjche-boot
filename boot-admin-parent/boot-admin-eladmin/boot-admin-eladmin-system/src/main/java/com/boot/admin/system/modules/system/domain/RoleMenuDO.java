package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 角色，菜单映射表
 * </p>
 *
 * @author miaoyj
 * @since 2021-07-12
 * @version 1.0.0-SNAPSHOT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_roles_menus")
public class RoleMenuDO implements Serializable {


    /**
     * 角色
     */
    @TableId
    private Long roleId;

    /**
     * 菜单
     */
    private Long menuId;
}
