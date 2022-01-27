BEGIN;

INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
 VALUES (1, 1, 1, '代码生成-学生菜单', 'StudentMenu', 'system/student/index', 999, 'Steve-Jobs', 'student', b'0', b'0', b'0', 'student:list', 'admin', 'admin', '2020-11-10 14:08:10', '2021-09-10 16:55:06');


-- 添加菜单-增，删，改
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'StudentMenu') AS temp), 0, 2, '学生测试菜单新增', 'student_add', '', 5, '', '', b'0', b'0', b'0', 'student:add', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'StudentMenu') AS temp), 0, 2, '学生测试菜单编辑', 'student_edit', '', 5, '', '', b'0', b'0', b'0', 'student:edit', 'admin', 'admin', now(), now());
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'StudentMenu') AS temp), 0, 2, '学生测试菜单删除', 'student_del', '', 5, '', '', b'0', b'0', b'0', 'student:del', 'admin', 'admin', now(), now());

-- 添加菜单-角色关联
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = 'student:list'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:add'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:edit'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:del'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_ADMIN'));

COMMIT;