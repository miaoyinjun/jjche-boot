-- 添加标识字段

ALTER TABLE `sys_role`
ADD COLUMN `code` varchar(50) NOT NULL COMMENT '标识' AFTER `name`;

UPDATE `sys_role` SET code = 'ROLE_ADMIN' WHERE name = '超级管理员';
UPDATE `sys_role` SET code = 'ROLE_DEMO', name = '演示角色' WHERE name = '普通用户';

ALTER TABLE `sys_role`
ADD UNIQUE INDEX `ux_code`(`code`) USING BTREE COMMENT '标识唯一索引';