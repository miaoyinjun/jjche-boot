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
		role_id = (SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO');

-- 删除数据
DELETE FROM sys_data_permission_rule;
DELETE FROM sys_data_permission_rule_role;
DELETE FROM sys_data_permission_field ;
DELETE FROM sys_data_permission_field_role;
COMMIT;