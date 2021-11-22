-- 删除菜单
 DELETE FROM `sys_roles_menus` WHERE
 		(menu_id = (SELECT id FROM sys_menu WHERE title = '数据权限管理'))
 		AND
 		role_id = (SELECT id FROM sys_role WHERE `name` = '超级管理员');

 DELETE FROM `sys_menu` WHERE title = '数据权限管理';

-- 增加字段
ALTER TABLE `sys_data_permission_field` ADD `is_activated` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否有效 1是 0否' AFTER sort;