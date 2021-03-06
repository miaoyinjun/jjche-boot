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

-- 添加菜单-角色关联
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = 'student:list'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:add'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:edit'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO')),
		((SELECT id FROM sys_menu WHERE `permission` = 'student:del'), (SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO'));


-- 测试数据-行
INSERT INTO `sys_data_permission_rule` (`menu_id`, `name`, `condition`, `column`, `value`, `is_activated`, `created_by`, `updated_by`, `gmt_create`, `gmt_modified`)
    VALUES ((SELECT id FROM sys_menu WHERE `permission` = 'student:list'), '只看年龄等于3的', 'EQUAL', 'age', '3', 1, 'System', 'System', now(), now());

INSERT INTO `sys_data_permission_rule_role` (`role_id`, `menu_id`, `data_permission_rule_id`, `created_by`, `updated_by`, `gmt_create`, `gmt_modified`)
    VALUES ((SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO'), (SELECT id FROM sys_menu WHERE `permission` = 'student:list'), (SELECT id FROM sys_data_permission_rule WHERE name = '只看年龄等于3的'), 'System', 'System', now(), now());

-- 测试数据-列
INSERT INTO `sys_data_permission_field` (`menu_id`, `name`, `code`, `sort`, `is_activated`) VALUES ((SELECT id FROM sys_menu WHERE name = 'StudentMenu'), '姓名', 'name', 999, 1);
INSERT INTO `sys_data_permission_field` (`menu_id`, `name`, `code`, `sort`, `is_activated`) VALUES ((SELECT id FROM sys_menu WHERE name = 'StudentMenu'), '性别', 'age', 999, 1);

INSERT INTO `sys_data_permission_field_role` (`role_id`, `menu_id`, `data_permission_field_id`, `is_accessible`, `is_editable`) VALUES ((SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO'), (SELECT id FROM sys_menu WHERE name = 'StudentMenu'), (SELECT id FROM sys_data_permission_field WHERE code = 'name'), 1, 0);
INSERT INTO `sys_data_permission_field_role` (`role_id`, `menu_id`, `data_permission_field_id`, `is_accessible`, `is_editable`) VALUES ((SELECT id FROM sys_role WHERE `code` = 'ROLE_DEMO'), (SELECT id FROM sys_menu WHERE name = 'StudentMenu'), (SELECT id FROM sys_data_permission_field WHERE code = 'age'), 1, 1);

COMMIT;