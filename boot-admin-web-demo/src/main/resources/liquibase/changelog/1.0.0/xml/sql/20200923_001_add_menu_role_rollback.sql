BEGIN;
-- 删除菜单--回滚
DELETE FROM `sys_roles_menus` WHERE
		(menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'student:list')
		OR
		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'student:add')
		OR
		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'student:edit')
		OR
		menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'student:del')
		)
		AND
		role_id = (SELECT id FROM sys_role WHERE `name` = '超级管理员');

DELETE FROM `sys_menu` WHERE
    title = '学生测试菜单' || title = '学生测试菜单新增' || title = '学生测试菜单编辑' || title = '学生测试菜单删除';
;
COMMIT;