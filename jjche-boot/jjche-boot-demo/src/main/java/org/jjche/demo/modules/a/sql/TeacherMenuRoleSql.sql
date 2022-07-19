BEGIN;

-- 添加菜单-列表
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
		VALUES
			((SELECT id FROM (SELECT id FROM sys_menu WHERE `title` = '系统管理') AS temp), 3, 1, 'ss管理', 'TeacherMenu', 'system/teacher/index', 999, 'tree', 'Teacher', b'0', b'0', b'0', 'teacher:list', 'admin', 'admin', now(), now());

-- 添加菜单-增，删，改
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'TeacherMenu') AS temp), 0, 2, 'ss新增', 'teacher_add', '', 5, '', '', b'0', b'0', b'0', 'teacher:add', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'TeacherMenu') AS temp), 0, 2, 'ss编辑', 'teacher_edit', '', 5, '', '', b'0', b'0', b'0', 'teacher:edit', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'TeacherMenu') AS temp), 0, 2, 'ss删除', 'teacher_del', '', 5, '', '', b'0', b'0', b'0', 'teacher:del', 'admin', 'admin', now(), now());

-- 添加菜单-角色关联
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = 'teacher:list'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'teacher:add'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'teacher:edit'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'teacher:del'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN'));

-- 删除菜单--回滚

-- DELETE FROM `sys_roles_menus` WHERE
-- 		(menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'teacher:list')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'teacher:add')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'teacher:edit')
-- 		OR
-- 		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'teacher:del')
-- 		)
-- 		AND
-- 		role_id = (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')
-- ;

-- DELETE FROM `sys_menu` WHERE
--     title = 'ss管理' || title = 'ss新增' || title = 'ss编辑' || title = 'ss删除';

COMMIT;