
CREATE DATABASE `security-jwt-db`;

USE `security-jwt-db`;

CREATE TABLE `t_user` (
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


INSERT INTO `t_user` VALUES (1001, 'root', '周杰伦', '$2a$10$HebtQPbLFf3YrO6B1n8Sb.AWHAz8SZtAc48IFGm8iSXjZsym3GPii', 0, '1550324080@qq.com', '18420163207', 0, 'none', '2022-09-26 23:46:02', '2022-09-26 23:46:05', 0);
