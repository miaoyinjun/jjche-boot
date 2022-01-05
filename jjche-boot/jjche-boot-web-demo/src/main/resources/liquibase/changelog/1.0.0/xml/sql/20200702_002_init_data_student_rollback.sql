DELETE FROM `student`
    WHERE
        (`name` = '王🐎')
        OR (`name` = '大')
        OR (`name` = '力');

DELETE FROM `sys_dict`
    WHERE
        (`name` = 'course_status');

DELETE FROM `sys_dict_detail`
    WHERE
        (`value` = '102' OR `value` = '103' OR
        `value` = '104' OR `value` = '105');