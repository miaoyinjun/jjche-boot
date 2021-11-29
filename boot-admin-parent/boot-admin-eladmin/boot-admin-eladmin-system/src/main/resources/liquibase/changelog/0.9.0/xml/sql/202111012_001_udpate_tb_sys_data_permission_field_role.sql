-- 增加字段
ALTER TABLE `sys_data_permission_field_role`
ADD `is_accessible` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可见 1是 0否' AFTER data_permission_field_id,
ADD `is_editable` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '是否可编辑 1是 0否' AFTER is_accessible;