/*
 Navicat Premium Data Transfer

 Source Server         : kaguya
 Source Server Type    : MySQL
 Source Server Version : 50722
 Source Host           : localhost:3306
 Source Schema         : kaguya

 Target Server Type    : MySQL
 Target Server Version : 50722
 File Encoding         : 65001

 Date: 17/12/2019 23:52:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin_o_auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_o_auth`;
CREATE TABLE `admin_o_auth`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `salt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐值',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员认证' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin_o_auth
-- ----------------------------
INSERT INTO `admin_o_auth` VALUES (2, 12410245256056832, 'bbfc1542d3ea9eef64c1c265336173764dfbe57539acc6d8c92ca2f118c58fd3', 'f4689e8e3d29399800ff01a856f7dd523f4a3c3a46af0f2fcd7ed585257e6b96');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `order_id` int(2) NOT NULL DEFAULT 0 COMMENT '展示顺序，从0开始',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `uk_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (6, 12410258996596736, '主页', 0);
INSERT INTO `category` VALUES (7, 10904984578166784, 'Java', 1);
INSERT INTO `category` VALUES (8, 12410259315363840, '设计模式', 2);
INSERT INTO `category` VALUES (9, 12410259449581568, 'Spring', 3);
INSERT INTO `category` VALUES (10, 12410259567022080, '数据结构', 4);

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'System' COMMENT '创建人',
  `update_user` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `views` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `uk_document_id`(`document_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档基本属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of document
-- ----------------------------
INSERT INTO `document` VALUES (1, 12411480092708864, '2019-12-17 23:22:20', NULL, 'AkaneMurakwa', NULL, 'Hello World', 0);
INSERT INTO `document` VALUES (2, 12411794464182272, '2019-12-17 23:22:39', NULL, 'AkaneMurakwa', NULL, 'int、long', 0);
INSERT INTO `document` VALUES (3, 12430146272235520, '2019-12-17 23:40:53', NULL, 'AkaneMurakwa', NULL, '```\npublic static void main(String args[]){\n	System.out.println(\"Hello World\");\n}\n```', 0);

-- ----------------------------
-- Table structure for document_group
-- ----------------------------
DROP TABLE IF EXISTS `document_group`;
CREATE TABLE `document_group`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `parent_id` bigint(11) NOT NULL COMMENT '父类id，0表示一级',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `order_id` int(2) NOT NULL DEFAULT 0 COMMENT '展示的顺序，从0开始',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `idx_document_id`(`document_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE,
  INDEX `idx_category_id`(`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '树形文档' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of document_group
-- ----------------------------
INSERT INTO `document_group` VALUES (1, 12411480092708864, 0, 10904984578166784, 0, '第一章 初始Java');
INSERT INTO `document_group` VALUES (2, 12411794464182272, 0, 10904984578166784, 1, '第二章 基本数据类型');
INSERT INTO `document_group` VALUES (3, 12430146272235520, 12411480092708864, 10904984578166784, 0, '第一个程序');

-- ----------------------------
-- Table structure for oauth
-- ----------------------------
DROP TABLE IF EXISTS `oauth`;
CREATE TABLE `oauth`  (
  `id` bigint(20) NOT NULL,
  `createdAt` bigint(20) NOT NULL,
  `expiresAt` bigint(20) NOT NULL,
  `updatedAt` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `authProviderId` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authToken` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UNI_AUTH`(`authProviderId`, `authId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `email` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱(登录名)',
  `sex` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'M' COMMENT '用户性别',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=' COMMENT '头像',
  `description` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '简单一句话，介绍你自己' COMMENT '简介',
  `github` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'github',
  `twitter` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'twitter',
  `weibo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'weibo',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `uk_email`(`email`) USING BTREE,
  UNIQUE INDEX `uk_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户基本信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (2, 12410245256056832, 'AkaneMurakwa', 'chenshjing@gmail.com', NULL, 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=', NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
