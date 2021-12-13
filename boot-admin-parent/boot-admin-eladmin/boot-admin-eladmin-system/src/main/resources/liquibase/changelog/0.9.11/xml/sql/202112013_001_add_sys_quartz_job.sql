-- 增加清空日志任务
INSERT INTO `sys_quartz_job`(bean_name, cron_expression, is_pause, job_name, description, method_name, params, person_in_charge, pause_after_failure)
VALUES
	(
		'logTask',
		'0 55 23 ? * *',
		0,
		'清空N个月之前的操作日志',
		'清空N个月之前的操作日志',
		'cleanLogs',
		'-3',
		'admin',
		0
	);