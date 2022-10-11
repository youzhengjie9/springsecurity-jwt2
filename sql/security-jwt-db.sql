/* rbac模型的权限数据库（经典的5张表）*/
/* 分别是用户表、角色表、菜单表（权限表）、用户-角色表、角色-菜单表 */

DROP DATABASE IF EXISTS `security-jwt-db`;
CREATE DATABASE `security-jwt-db`;

USE `security-jwt-db`;

/* 用户表 */
DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
                            `id` bigint NOT NULL COMMENT '主键',
                            `user_name` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '用户名',
                            `nick_name` VARCHAR(64) NOT NULL DEFAULT 'NULL' COMMENT '昵称',
                            `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '密码',
                            `status` tinyint(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
                            `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
                            `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
                            `sex` tinyint(1) DEFAULT NULL COMMENT '用户性别（0男，1女，2未知）',
                            `avatar` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '头像地址',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `user_name` (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';


INSERT INTO `sys_user` VALUES (1001, 'root', '周杰伦', '$2a$10$HebtQPbLFf3YrO6B1n8Sb.AWHAz8SZtAc48IFGm8iSXjZsym3GPii', 0, '1550324080@qq.com', '18420163207', 0, 'https://img2.baidu.com/it/u=670341883,3643142939&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);
INSERT INTO `sys_user` VALUES (1002, 'test', '蔡徐坤', '$2a$10$HebtQPbLFf3YrO6B1n8Sb.AWHAz8SZtAc48IFGm8iSXjZsym3GPii', 0, '1550324080@qq.com', '18420163207', 0, 'https://img2.baidu.com/it/u=361550957,796293689&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);


/* 角色表 */
DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
                            `id` bigint(20) NOT NULL COMMENT '主键',
                            `name` varchar(128) DEFAULT NULL COMMENT '角色权限名称，比如管理员',
                            `role_key` varchar(100) DEFAULT NULL COMMENT '角色权限关键字，比如admin',
                            `status` tinyint(1) DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
                            `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色表';


INSERT INTO sys_role VALUES (2001,'管理员','admin',0,0,'2022-09-26 23:46:02','2022-09-28 23:46:02','管理员角色');
INSERT INTO sys_role VALUES (2002,'普通用户','user',0,0,'2022-09-25 10:23:02','2022-09-28 10:33:02','普通用户角色');
INSERT INTO sys_role VALUES (2003,'黑名单用户','blackListUser',0,0,'2022-09-25 10:23:02','2022-09-28 10:33:02','黑名单角色');


/* 菜单表。也就是后台侧边栏菜单（本质上其实也是接口菜单权限）与接口菜单权限的表 */
DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
                            `id` bigint(20) NOT NULL COMMENT '主键',
                            `parent_id` bigint(20) DEFAULT '0' COMMENT '后台侧边栏。父菜单ID,一级菜单为0',
                            `menu_name` varchar(64) NOT NULL DEFAULT 'NULL' COMMENT '菜单/权限名称',
                            `path` varchar(200) DEFAULT NULL COMMENT 'vue路由地址（type=1才会生效，type=2不生效）',
                            `component` varchar(255) DEFAULT NULL COMMENT '动态路由要用到。views目录下的组件页面名,自动会补上前缀../views（type=1才会生效，type=2不生效）',
                            `status` tinyint(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
                            `visible` tinyint(1) DEFAULT '0' COMMENT '菜单显示状态（0显示 1隐藏）（type=0或者1才会生效，type=2不生效）',
                            `perms` varchar(100) DEFAULT NULL COMMENT '前后端的权限标识，比如sys:user:list',
                            `type` int NOT NULL COMMENT '菜单类型。0：目录（点击后台侧边栏可以展开成下一级菜单的按钮）;1：菜单（点击后台侧边栏直接跳转vue路由组件的按钮）;2：普通后台服务器接口;（与后台侧边栏无关，仅仅是普通接口权限）',
                            `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标（type=0或者1才会生效，type=2不生效）',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '最后一次修改时间',
                            `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志（0代表未删除，1代表已删除）',
                            `sort` int DEFAULT '1' COMMENT '前端菜单排序，默认值为1，1的优先级最高，排在最上面',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单表';

INSERT INTO sys_menu VALUES (3001,0,'仪表盘','/dashboard','/dashboard/index.vue',0,0,null,1,'el-icon-s-home','2022-09-26 23:46:02','2022-09-28 23:46:02',0,1,'bz');
INSERT INTO sys_menu VALUES (3002,0,'用户管理','/user/list','/user-list/index.vue',0,0,'vue:sys:user:list',1,'el-icon-s-order','2022-09-26 23:46:02','2022-09-28 23:46:02',0,2,'bz');
INSERT INTO sys_menu VALUES (3003,0,'角色管理','/role/list','/role-list/index.vue',0,0,'vue:sys:role:list',1,'el-icon-suitcase','2022-09-26 23:46:02','2022-09-28 23:46:02',0,3,'bz');
INSERT INTO sys_menu VALUES (3004,0,'菜单管理','/menu/list','/menu-list/index.vue',0,0,'vue:sys:menu:list',1,'el-icon-s-custom','2022-09-26 23:46:02','2022-09-28 23:46:02',0,4,'bz');

INSERT INTO sys_menu VALUES (3005,0,'一级菜单',null,null,0,0,'vue:sys:first',0,'el-icon-s-tools','2022-09-26 23:46:02','2022-09-28 23:46:02',0,5,'bz');
INSERT INTO sys_menu VALUES (3006,3005,'二级菜单',null,null,0,0,'vue:sys:second',0,'el-icon-eleme','2022-09-26 23:46:02','2022-09-28 23:46:02',0,6,'bz');
INSERT INTO sys_menu VALUES (3007,3006,'三级菜单1','/page1','/test-page/page1.vue',0,0,'vue:sys:third1',1,'el-icon-suitcase','2022-09-26 23:46:02','2022-09-28 23:46:02',0,9,'bz');
INSERT INTO sys_menu VALUES (3008,3006,'三级菜单2','/page2','/test-page/page2.vue',0,0,'vue:sys:third2',1,'el-icon-tickets','2022-09-26 23:46:02','2022-09-28 23:46:02',0,7,'bz');
INSERT INTO sys_menu VALUES (3009,3006,'三级菜单3','/page3','/test-page/page3.vue',0,0,'vue:sys:third3',1,'el-icon-tickets','2022-09-26 23:46:02','2022-09-28 23:46:02',0,8,'bz');


INSERT INTO sys_menu VALUES (3010,0,'后台服务器接口=/test',null,null,0,0,'sys:test',2,null,'2022-09-26 23:46:02','2022-09-28 23:46:02',0,1,'bz');
INSERT INTO sys_menu VALUES (3011,0,'后台服务器接口=/test2',null,null,0,0,'sys:test2',2,null,'2022-09-26 23:46:02','2022-09-28 23:46:02',0,1,'bz');
INSERT INTO sys_menu VALUES (3012,0,'后台服务器接口=/test3',null,null,0,0,'sys:test3',2,null,'2022-09-26 23:46:02','2022-09-28 23:46:02',0,1,'bz');


/* 用户-角色关联表 */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
                                 `id` bigint(20) NOT NULL COMMENT '主键',
                                 `user_id` bigint(200) NOT NULL COMMENT '用户id',
                                 `role_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '角色id',
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户-角色表';

INSERT INTO sys_user_role VALUES (4001,1001,2001);
INSERT INTO sys_user_role VALUES (4002,1001,2002);
INSERT INTO sys_user_role VALUES (4003,1002,2002);


/* 角色-菜单关联表 */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
                                 `id` bigint(20) NOT NULL COMMENT '主键',
                                 `role_id` bigint(200) NOT NULL COMMENT '角色ID',
                                 `menu_id` bigint(200) NOT NULL DEFAULT '0' COMMENT '菜单id',
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色-菜单表';

-- 管理员菜单
INSERT INTO sys_role_menu VALUES (5001,2001,3001);
INSERT INTO sys_role_menu VALUES (5002,2001,3002);
INSERT INTO sys_role_menu VALUES (5003,2001,3003);
INSERT INTO sys_role_menu VALUES (5004,2001,3004);
INSERT INTO sys_role_menu VALUES (5005,2001,3005);
INSERT INTO sys_role_menu VALUES (5006,2001,3006);
INSERT INTO sys_role_menu VALUES (5007,2001,3007);
INSERT INTO sys_role_menu VALUES (5008,2001,3008);
INSERT INTO sys_role_menu VALUES (5009,2001,3009);

INSERT INTO sys_role_menu VALUES (5010,2001,3010);


-- 普通用户菜单

INSERT INTO sys_role_menu VALUES (5013,2002,3001);
INSERT INTO sys_role_menu VALUES (5014,2002,3005);
INSERT INTO sys_role_menu VALUES (5015,2002,3006);
INSERT INTO sys_role_menu VALUES (5016,2002,3007);
INSERT INTO sys_role_menu VALUES (5017,2002,3008);
INSERT INTO sys_role_menu VALUES (5018,2002,3002);
INSERT INTO sys_role_menu VALUES (5019,2002,3004);

INSERT INTO sys_role_menu VALUES (5020,2002,3010);
INSERT INTO sys_role_menu VALUES (5021,2002,3011);

-- 黑名单用户菜单

INSERT INTO sys_role_menu VALUES (5022,2003,3005);
INSERT INTO sys_role_menu VALUES (5023,2003,3006);
INSERT INTO sys_role_menu VALUES (5024,2003,3002);
INSERT INTO sys_role_menu VALUES (5025,2003,3004);

INSERT INTO sys_role_menu VALUES (5026,2003,3010);

