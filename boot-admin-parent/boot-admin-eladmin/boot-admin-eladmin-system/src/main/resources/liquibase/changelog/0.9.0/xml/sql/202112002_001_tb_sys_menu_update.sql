BEGIN;
-- 修改在线用户菜单权限标识
UPDATE sys_menu SET permission = 'online:list' WHERE name = 'OnlineUser';
-- 插入强退按钮
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'OnlineUser') AS temp), 0, 2, '强退', 'online_del', '', 5, '', '', b'0', b'0', b'0', 'online:del', 'admin', 'admin', now(), now());

-- 修改日志菜单权限标识
UPDATE sys_menu SET permission = 'log:list' WHERE name = 'Log';
-- 插入清空按钮
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
	VALUES ((SELECT id FROM (SELECT id FROM sys_menu WHERE `name` = 'Log') AS temp), 0, 2, '清空', 'log_del', '', 5, '', '', b'0', b'0', b'0', 'log:del', 'admin', 'admin', now(), now());

-- 修改代码生成菜单权限标识
UPDATE sys_menu SET permission = 'generator:list', sub_count = 2 WHERE name = 'GeneratorIndex';

-- 添加普通用户角色关联权限
INSERT INTO `sys_roles_menus`
	VALUES
		((SELECT id FROM sys_menu WHERE `permission` = 'version:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'online:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `name` = 'CacheMonitor'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'generator:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'storage:add'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'storage:edit'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'storage:del'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'app:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'deploy:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'deployHistory:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE `permission` = 'database:list'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE  name = 'Preview'), (SELECT id FROM sys_role WHERE `name` = '普通用户')),
		((SELECT id FROM sys_menu WHERE  name = 'GeneratorConfig'), (SELECT id FROM sys_role WHERE `name` = '普通用户'));
COMMIT