-- 增加首页菜单
INSERT INTO `sys_menu`(pid, sub_count, type, title, `name`, component, menu_sort, icon, path, i_frame, `cache`, hidden, permission, created_by, updated_by, gmt_create, gmt_modified)
		VALUES
			(null, 0, 0, '首页', 'Dashboard', 'Layout', 0, 'index', 'dashboard', b'0', b'0', b'0', 'dashboard:list', 'admin', 'admin', now(), now());