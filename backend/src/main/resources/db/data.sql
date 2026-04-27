-- 基金管理系统 初始化数据
USE fund_admin;

-- 默认管理员用户
-- 用户名: admin  密码: admin123
-- BCrypt哈希值由 Spring Security 的 BCryptPasswordEncoder 生成，强度10
INSERT INTO sys_user (id, username, password, nickname, avatar, email, phone, status, remark)
VALUES (1, 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '超级管理员', NULL, 'admin@fundadmin.com', NULL, 1, '默认管理员账号');

-- 默认角色
INSERT INTO sys_role (id, role_name, role_code, sort, status, remark)
VALUES (1, '超级管理员', 'admin', 1, 1, '拥有所有权限');

-- 默认菜单(基础)
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission, icon, sort, visible, status, remark)
VALUES (1, 0, '系统管理', 1, '/system', NULL, NULL, 'Setting', 1, 1, 1, '系统管理目录');
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission, icon, sort, visible, status, remark)
VALUES (2, 1, '用户管理', 2, '/system/user', 'system/user/index', 'system:user:list', 'User', 1, 1, 1, '用户管理菜单');
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission, icon, sort, visible, status, remark)
VALUES (3, 1, '角色管理', 2, '/system/role', 'system/role/index', 'system:role:list', 'Lock', 2, 1, 1, '角色管理菜单');
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission, icon, sort, visible, status, remark)
VALUES (4, 1, '菜单管理', 2, '/system/menu', 'system/menu/index', 'system:menu:list', 'Menu', 3, 1, 1, '菜单管理菜单');
INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, permission, icon, sort, visible, status, remark)
VALUES (5, 0, '仪表盘', 2, '/dashboard', 'dashboard/index', NULL, 'Dashboard', 0, 1, 1, '首页仪表盘');

-- 用户角色关联(admin -> admin角色)
INSERT INTO sys_user_role (id, user_id, role_id)
VALUES (1, 1, 1);

-- 角色菜单关联(admin角色 -> 所有菜单)
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (1, 1, 1);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (2, 1, 2);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (3, 1, 3);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (4, 1, 4);
INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (5, 1, 5);