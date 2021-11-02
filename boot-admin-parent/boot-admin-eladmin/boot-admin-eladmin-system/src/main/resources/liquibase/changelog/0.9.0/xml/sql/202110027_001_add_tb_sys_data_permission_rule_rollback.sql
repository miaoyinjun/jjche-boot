BEGIN;

DROP TABLE `sys_data_permission_rule`;
DROP TABLE `sys_data_permission_rule_role`;

-- 删除菜单--回滚

 DELETE FROM `sys_roles_menus` WHERE
 		(menu_id = (SELECT id FROM sys_menu WHERE `permission` = 'sysDataPermissionRule:list'))
 		AND
 		role_id = (SELECT id FROM sys_role WHERE `name` = '超级管理员')
 ;

 DELETE FROM `sys_menu` WHERE
     title = '数据规则';


DELETE FROM `sys_dict_detail`
    WHERE dict_id in (select id from sys_dict where `name` = 'dataPermissionRule_condition');

DELETE FROM `sys_dict`
    WHERE
        (`name` = 'dataPermissionRule_condition');
COMMIT;