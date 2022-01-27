INSERT INTO `student` ( `name`, `age`, `dept_id`)
    VALUES
    ( '王🐎', 1, 103),
    ( '大', 2, 104),
    ( '力', 3, 105);

INSERT INTO `sys_dict`
(`name`, `description`, `created_by`, `updated_by`, `gmt_create`, `gmt_modified`)
VALUES ('course_status', '课程状态', 'System', 'System', '2021-09-13 13:15:30', '2021-09-13 13:15:30');

INSERT INTO `sys_dict_detail`
(`dict_id`, `label`, `value`, `dict_sort`)
VALUES ((SELECT id FROM sys_dict WHERE name = 'course_status'),
'图文', '102', 1),
((SELECT id FROM sys_dict WHERE name = 'course_status'),
'音频', '103', 2),
((SELECT id FROM sys_dict WHERE name = 'course_status'),
'视频', '104', 3),
((SELECT id FROM sys_dict WHERE name = 'course_status'),
'外链', '105', 4);
