BEGIN;

-- 添加菜单-列表
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
		VALUES
			((SELECT id FROM (SELECT id FROM sys_menu WHERE `title` = '系统管理') AS temp), 3, 1, '${apiAlias}管理', '${className}Menu', 'system/${changeClassName}/index', 999, 'tree', '${className}', b'0', b'0', b'0', '${changeClassName}:list', 'admin', 'admin', now(), now());

-- 添加菜单-增，删，改
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}新增', '${changeClassName}_add', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:add', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}编辑', '${changeClassName}_edit', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:edit', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = '${className}Menu') AS temp), 0, 2, '${apiAlias}删除', '${changeClassName}_del', '', 5, '', '', b'0', b'0', b'0', '${changeClassName}:del', 'admin', 'admin', now(), now());

-- 添加菜单-角色关联
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:list'), (SELECT id FROM sys_role WHERE `name` = '超级管理员')),
		((SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:add'), (SELECT id FROM sys_role WHERE `name` = '超级管理员')),
		((SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:edit'), (SELECT id FROM sys_role WHERE `name` = '超级管理员')),
		((SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:del'), (SELECT id FROM sys_role WHERE `name` = '超级管理员'));

-- 删除菜单--回滚

-- DELETE FROM `sys_roles_menus` WHERE
-- 		(menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:list')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:add')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:edit')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = '${changeClassName}:del')
-- 		)
-- 		AND
-- 		role_id = (SELECT id FROM sys_role WHERE `name` = '超级管理员')
-- ;

-- DELETE FROM `sys_menu` WHERE
--     title = '${apiAlias}管理' || title = '${apiAlias}新增' || title = '${apiAlias}编辑' || title = '${apiAlias}删除';

COMMIT;