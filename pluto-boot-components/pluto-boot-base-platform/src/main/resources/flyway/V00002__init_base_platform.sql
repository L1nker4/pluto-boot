SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
                            `dept_id` bigint(60) NOT NULL COMMENT '部门ID',
                            `parent_id` bigint(60) DEFAULT NULL COMMENT '上级部门ID',
                            `dept_name` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
                            `sort` double DEFAULT NULL COMMENT '排序',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            PRIMARY KEY (`dept_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门表';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `sort`, `create_time`, `update_time`) VALUES (1218797128664621057, 0, '开发部', 1, '2020-01-19 07:27:34', '2020-01-22 21:48:06');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `sort`, `create_time`, `update_time`) VALUES (1219980116765151233, 1218797128664621057, '后端组', 1, '2020-01-22 21:48:21', '2020-01-22 21:49:34');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `dept_name`, `sort`, `create_time`, `update_time`) VALUES (1219980165251305473, 1218797128664621057, '前端组', 2, '2020-01-22 21:48:32', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
                            `dict_id` bigint(60) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
                            `key` bigint(20) NOT NULL COMMENT '键',
                            `value` varchar(100) COLLATE utf8_general_ci NOT NULL COMMENT '值',
                            `field_name` varchar(100) COLLATE utf8_general_ci NOT NULL COMMENT '字段名称',
                            `table_name` varchar(100) COLLATE utf8_general_ci NOT NULL COMMENT '表名',
                            PRIMARY KEY (`dict_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=30008;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (1, 0, '男', 'sex', 'tb_user');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (2, 1, '女', 'sex', 'tb_user');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (3, 2, '保密', 'ssex', 'tb_user');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (4, 1, '有效', 'status', 'tb_user');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (5, 0, '锁定', 'status', 'tb_user');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (6, 0, '菜单', 'type', 'tb_menu');
INSERT INTO `sys_dict` (`dict_id`, `key`, `value`, `field_name`, `table_name`) VALUES (7, 1, '按钮', 'type', 'tb_menu');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
                           `id` bigint(60) NOT NULL COMMENT '日志ID',
                           `username` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作用户',
                           `operation` text COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作内容',
                           `time` decimal(11,0) DEFAULT NULL COMMENT '耗时',
                           `method` text COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作方法',
                           `params` text COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作参数',
                           `ip` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作IP',
                           `create_time` datetime DEFAULT NULL COMMENT '操作时间',
                           `location` varchar(50) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作地点',
                           PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log` (
                                 `id` bigint(60) NOT NULL COMMENT '主键ID',
                                 `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                 `login_time` datetime DEFAULT NULL COMMENT '登陆时间',
                                 `location` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登陆地址',
                                 `ip` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'IP地址',
                                 PRIMARY KEY (`id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `menu_id` bigint(60) NOT NULL COMMENT '菜单/按钮ID',
                            `parent_id` bigint(60) DEFAULT NULL COMMENT '父级菜单ID',
                            `menu_name` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单/按钮ID',
                            `path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应路由Path',
                            `component` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '对应路由组件component',
                            `perms` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
                            `icon` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
                            `type` char(2) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型 0菜单 1按钮',
                            `sort` double DEFAULT NULL COMMENT '排序',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            PRIMARY KEY (`menu_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219221934328348673, 0, '系统管理', '/system', 'PageView', NULL, 'setting', '0', 1, '2020-01-20 11:35:35', '2020-01-23 16:01:29');
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219222402060279809, 0, '系统监控', '/monitor', 'PageView', NULL, 'dashboard', '0', 2, '2020-01-20 11:37:27', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219223716672602114, 1219221934328348673, '用户管理', '/system/user', 'system/user/User', 'user:view', '', '0', 1, '2020-01-20 11:42:40', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219224625301487617, 1219221934328348673, '角色管理', '/system/role', 'system/role/Role', 'role:view', '', '0', 2, '2020-01-20 11:46:17', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219225012158926849, 1219221934328348673, '菜单管理', '/system/menu', 'system/menu/Menu', 'menu:view', '', '0', 3, '2020-01-20 11:47:49', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219225208368455682, 1219221934328348673, '部门管理', '/system/dept', 'system/dept/Dept', 'dept:view', '', '0', 4, '2020-01-20 11:48:36', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219225492503171074, 1219222402060279809, '在线用户', '/monitor/online', 'monitor/Online', 'user:online', '', '0', 1, '2020-01-20 11:49:44', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219225627626885122, 1219222402060279809, '系统日志', '/monitor/systemlog', 'monitor/SystemLog', 'log:view', '', '0', 2, '2020-01-20 11:50:16', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219226126266720258, 1219221934328348673, '字典管理', '/system/dict', 'system/dict/Dict', 'dict:view', '', '0', 5, '2020-01-20 11:52:15', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219226478202343425, 1219222402060279809, 'Redis监控', '/monitor/redis/info', 'monitor/RedisInfo', 'redis:view', '', '0', 3, '2020-01-20 11:53:39', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219226788459302913, 1219222402060279809, '系统信息', '/monitor/system', 'EmptyPageView', '', '', '0', 5, '2020-01-20 11:54:53', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219227219763765249, 1219226788459302913, 'Tomcat信息', '/monitor/system/tomcatinfo', 'monitor/TomcatInfo', 'monitor/Httptrace', '', '0', 1, '2020-01-20 11:56:36', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219228097522552833, 1219226788459302913, '服务器信息', '/monitor/system/info', 'monitor/SystemInfo', NULL, '', '0', 2, '2020-01-20 12:00:05', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219228261502955521, 1219226788459302913, 'JVM信息', '/monitor/system/jvminfo', 'monitor/JvmInfo', NULL, '', '0', 3, '2020-01-20 12:00:44', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229062216601601, 1219223716672602114, '新增用户', NULL, NULL, 'user:add', NULL, '1', NULL, '2020-01-20 12:03:55', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229160933793794, 1219223716672602114, '修改用户', NULL, NULL, 'user:update', NULL, '1', NULL, '2020-01-20 12:04:18', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229275245363202, 1219223716672602114, '删除用户', NULL, NULL, 'user:delete', NULL, '1', NULL, '2020-01-20 12:04:46', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229384527953922, 1219224625301487617, '新增角色', NULL, NULL, 'role:add', NULL, '1', NULL, '2020-01-20 12:05:12', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229479889580034, 1219224625301487617, '修改角色', NULL, NULL, 'role:update', NULL, '1', NULL, '2020-01-20 12:05:34', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229564576768001, 1219224625301487617, '删除角色', NULL, NULL, 'role:delete', NULL, '1', NULL, '2020-01-20 12:05:55', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229801676595202, 1219225012158926849, '新增菜单', NULL, NULL, 'menu:add', NULL, '1', NULL, '2020-01-20 12:06:51', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229882605740034, 1219225012158926849, '修改菜单', NULL, NULL, 'menu:update', NULL, '1', NULL, '2020-01-20 12:07:10', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219229962687545346, 1219225012158926849, '删除菜单', NULL, NULL, 'menu:delete', NULL, '1', NULL, '2020-01-20 12:07:30', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219230577069133826, 1219225208368455682, '新增部门', NULL, NULL, 'dept:add', NULL, '1', NULL, '2020-01-20 12:09:56', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219230644974944257, 1219225208368455682, '修改部门', NULL, NULL, 'dept:update', NULL, '1', NULL, '2020-01-20 12:10:12', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219230820431163393, 1219225492503171074, '踢出用户', NULL, NULL, 'user:kickout', NULL, '1', NULL, '2020-01-20 12:10:54', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219230935367675906, 1219225627626885122, '删除日志', NULL, NULL, 'log:delete', NULL, '1', NULL, '2020-01-20 12:11:22', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231080536621058, 1219226126266720258, '新增字典', NULL, NULL, 'dict:add', NULL, '1', NULL, '2020-01-20 12:11:56', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231156579356674, 1219226126266720258, '修改字典', NULL, NULL, 'dict:update', NULL, '1', NULL, '2020-01-20 12:12:14', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231571496669186, 1219223716672602114, '导出Excel', NULL, NULL, 'user:export', NULL, '1', NULL, '2020-01-20 12:13:53', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231648286068737, 1219224625301487617, '导出Excel', NULL, NULL, 'role:export', NULL, '1', NULL, '2020-01-20 12:14:11', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231725800914946, 1219225012158926849, '导出Excel', NULL, NULL, 'menu:export', NULL, '1', NULL, '2020-01-20 12:14:30', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231805891268610, 1219225208368455682, '导出Excel', NULL, NULL, 'dept:export', NULL, '1', NULL, '2020-01-20 12:14:49', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219231927173746690, 1219226126266720258, '导出Excel', NULL, NULL, 'dict:export', NULL, '1', NULL, '2020-01-20 12:15:18', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1219232059130748930, 1219223716672602114, '导出Excel', NULL, NULL, 'user:reset', NULL, '1', NULL, '2020-01-20 12:15:49', NULL);
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `icon`, `type`, `sort`, `create_time`, `update_time`) VALUES (1223101098564493313, 0, '测试管理', '/test', 'test/test', 'test:view', 'user', '0', 3, '2020-01-31 12:30:01', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `role_id` bigint(60) NOT NULL COMMENT '角色ID',
                            `role_name` varchar(20) COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
                            `remark` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            PRIMARY KEY (`role_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_time`, `update_time`) VALUES (1218804579426324481, '超级管理员', '超级管理员12', '2020-01-19 07:57:10', '2020-01-31 12:30:11');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint(60) DEFAULT NULL COMMENT '角色ID',
                                 `menu_id` bigint(60) DEFAULT NULL COMMENT '菜单ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219221934328348673);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219223716672602114);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229062216601601);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229160933793794);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229275245363202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231571496669186);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219232059130748930);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219224625301487617);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229384527953922);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229479889580034);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229564576768001);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231648286068737);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219225012158926849);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229801676595202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229882605740034);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219229962687545346);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231725800914946);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219225208368455682);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219230577069133826);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219230644974944257);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231805891268610);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219226126266720258);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231080536621058);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231156579356674);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219231927173746690);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219222402060279809);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219225492503171074);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219230820431163393);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219225627626885122);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219230935367675906);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219226478202343425);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219226617902116865);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219226788459302913);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219227219763765249);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219228097522552833);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219228261502955521);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324500, 1219223137221165058);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219221934328348673);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219222402060279809);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219225492503171074);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219230820431163393);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219223716672602114);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1219958879645057000, 1219229062216601601);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219221934328348673);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219222402060279809);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219223137221165058);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219223716672602114);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219224625301487617);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219225012158926849);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219225208368455682);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219225492503171074);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219225627626885122);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219226126266720258);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219226478202343425);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219226617902116865);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219226788459302913);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219227219763765249);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219228097522552833);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219228261502955521);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229062216601601);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229160933793794);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229275245363202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229384527953922);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229479889580034);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229564576768001);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229801676595202);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229882605740034);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219229962687545346);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219230577069133826);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219230644974944257);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219230820431163393);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219230935367675906);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231080536621058);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231156579356674);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231571496669186);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231648286068737);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231725800914946);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231805891268610);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219231927173746690);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1219232059130748930);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (1218804579426324481, 1223101098564493313);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `user_id` bigint(60) NOT NULL COMMENT '用户ID',
                            `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                            `password` varchar(128) COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                            `dept_id` bigint(60) DEFAULT NULL COMMENT '部门ID',
                            `email` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
                            `mobile` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系电话',
                            `status` char(1) COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态 0锁定 1有效',
                            `create_time` datetime NOT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                            `sex` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别 0男 1女 2保密',
                            `description` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
                            `avatar` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
                            PRIMARY KEY (`user_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `dept_id`, `email`, `mobile`, `status`, `create_time`, `update_time`, `last_login_time`, `sex`, `description`, `avatar`) VALUES (1218814424078544898, 'admin', 'e47faf93c0d84bd34df6acc217877dd3', 1219980116765151233, '927022262@qq.com', '13912603149', '1', '2020-01-19 08:36:17', '2020-07-03 14:47:41', '2020-07-03 14:47:56', '0', '管理员用户', 'cnrhVkzwxjPwAaCfPbdc.png');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_config`;
CREATE TABLE `sys_user_config` (
                                   `user_id` bigint(60) NOT NULL COMMENT '用户ID',
                                   `theme` varchar(10) COLLATE utf8_general_ci DEFAULT NULL COMMENT '系统主题 dark暗色风格，light明亮风格',
                                   `layout` varchar(10) COLLATE utf8_general_ci DEFAULT NULL COMMENT '系统布局 side侧边栏，head顶部栏',
                                   `multi_page` char(1) COLLATE utf8_general_ci DEFAULT NULL COMMENT '页面风格 1多标签页 0单页',
                                   `fix_siderbar` char(1) COLLATE utf8_general_ci DEFAULT NULL COMMENT '页面滚动是否固定侧边栏 1固定 0不固定',
                                   `fix_header` char(1) COLLATE utf8_general_ci DEFAULT NULL COMMENT '页面滚动是否固定顶栏 1固定 0不固定',
                                   `color` varchar(20) COLLATE utf8_general_ci DEFAULT NULL COMMENT '主题颜色 RGB值',
                                   PRIMARY KEY (`user_id`) /*T![clustered_index] CLUSTERED */
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- ----------------------------
-- Records of sys_user_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_config` (`user_id`, `theme`, `layout`, `multi_page`, `fix_siderbar`, `fix_header`, `color`) VALUES (1218814424078544898, 'dark', 'side', '0', '1', '1', 'rgb(47, 84, 235)');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint(60) NOT NULL COMMENT '用户ID',
                                 `role_id` bigint(60) DEFAULT NULL COMMENT '角色ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1218814424078544898, 1218804579426324481);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
