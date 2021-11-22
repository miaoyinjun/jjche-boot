INSERT INTO `sys_menu` VALUES (119, 1, 0, 1, '数据权限管理', 'DataPermission', 'system/data_permission/index', 6, 'permission', 'data_permission', b'0', b'0', b'0', 'data_permission:list', 'admin', 'admin', '2020-10-30 09:07:50', '2021-09-10 16:55:06');
INSERT INTO `sys_roles_menus` VALUES (119, 1);
ALTER TABLE `sys_data_permission_field` DROP COLUMN `is_activated`;