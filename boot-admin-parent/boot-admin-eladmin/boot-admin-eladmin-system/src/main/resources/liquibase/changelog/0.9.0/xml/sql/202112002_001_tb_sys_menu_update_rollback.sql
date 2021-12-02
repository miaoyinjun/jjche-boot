BEGIN;

-- 添加普通用户角色关联权限
DELETE FROM `sys_roles_menus`
WHERE (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'version:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'online:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `name` = 'CacheMonitor') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'generator:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'storage:add') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'storage:edit') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'storage:del') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'app:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'deploy:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'deployHistory:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'database:list') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE `name` = 'Preview') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
    OR
    (menu_id = (SELECT id FROM sys_menu WHERE name = 'GeneratorConfig') AND role_id = (SELECT id FROM sys_role WHERE `name` = '普通用户'))
;

-- 修改在线用户菜单权限标识
UPDATE sys_menu SET permission = NULL WHERE name = 'OnlineUser';
-- 插入强退按钮
DELETE FROM sys_menu WHERE permission = 'online:del';

-- 修改日志菜单权限标识
UPDATE sys_menu SET permission = NULL WHERE name = 'Log';
-- 插入清空按钮
DELETE FROM sys_menu WHERE permission = 'log:del';

-- 修改代码生成菜单权限标识
UPDATE sys_menu SET permission = NULL WHERE name = 'GeneratorIndex';

COMMIT;