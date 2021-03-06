CREATE TABLE `student` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`name` VARCHAR (50) NOT NULL COMMENT '姓名',
	`age` TINYINT UNSIGNED NOT NULL COMMENT '年龄',
	`dept_id` BIGINT UNSIGNED NOT NULL COMMENT '部门id',
    `created_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '创建者',
    `updated_by` varchar(50) NOT NULL DEFAULT 'System' COMMENT '修改者',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='测试学生';