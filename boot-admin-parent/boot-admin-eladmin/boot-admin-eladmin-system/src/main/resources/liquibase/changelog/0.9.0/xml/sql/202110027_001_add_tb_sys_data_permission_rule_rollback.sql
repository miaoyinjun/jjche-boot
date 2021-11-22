BEGIN;

DROP TABLE `sys_data_permission_rule`;
DROP TABLE `sys_data_permission_rule_role`;

DELETE FROM `sys_dict_detail`
    WHERE dict_id in (select id from sys_dict where `name` = 'dataPermissionRule_condition');

DELETE FROM `sys_dict`
    WHERE
        (`name` = 'dataPermissionRule_condition');
COMMIT;