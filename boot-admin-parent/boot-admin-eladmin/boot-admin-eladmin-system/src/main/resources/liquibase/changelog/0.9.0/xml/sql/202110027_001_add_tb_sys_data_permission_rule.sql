BEGIN;

CREATE TABLE `sys_data_permission_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `condition` varchar(50) NOT NULL COMMENT '条件',
  `column` varchar(50) DEFAULT NULL COMMENT '列名',
  `value` varchar(500) DEFAULT NULL COMMENT '规则值',
  `is_activated` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否有效 1是 0否',
  `created_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
  `updated_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='数据规则表';

 CREATE TABLE `sys_data_permission_rule_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  `data_permission_rule_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '数据规则权限ID',
  `created_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
  `updated_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='菜单数据规则权限角色表';

-- 添加字典
INSERT INTO `sys_dict`
(`name`, `description`, `created_by`, `updated_by`, `gmt_create`, `gmt_modified`)
VALUES ('dataPermissionRule_condition', '数据规则-条件', 'System', 'System', now(), now());

INSERT INTO `sys_dict_detail`
(`dict_id`, `label`, `value`, `dict_sort`)
VALUES
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'等于', 'EQUAL', 1),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'不等于', 'NE', 2),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'大于', 'GT', 3),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'大于等于', 'GE', 4),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'小于', 'LT', 5),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'小于等于', 'LE', 6),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'全模糊', 'LIKE', 7),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'左模糊', 'LEFT_LIKE', 8),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'右模糊', 'RIGHT_LIKE', 9),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'区间', 'BETWEEN', 10),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'包含', 'IN', 11),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'不为空', 'NOT_NULL', 12),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'为空', 'IS_NULL', 13),
((SELECT id FROM sys_dict WHERE name = 'dataPermissionRule_condition'),'自定义SQL片段', 'USE_SQL_RULES', 14);
COMMIT;