/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50716
Source Host           : localhost:3306
Source Database       : kaguya

Target Server Type    : MYSQL
Target Server Version : 50716
File Encoding         : 65001

Date: 2019-12-17 11:13:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin_o_auth
-- ----------------------------
DROP TABLE IF EXISTS `admin_o_auth`;
CREATE TABLE `admin_o_auth` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `salt` varchar(64) NOT NULL COMMENT '盐值',
  `password` varchar(64) NOT NULL COMMENT '密码',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='管理员认证';

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `order_id` int(2) NOT NULL DEFAULT '0' COMMENT '展示顺序，从0开始',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='分类';

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `create_user` varchar(45) NOT NULL DEFAULT 'System' COMMENT '创建人',
  `update_user` varchar(45) DEFAULT NULL COMMENT '更新人',
  `content` text NOT NULL COMMENT '内容',
  `views` int(11) DEFAULT '0' COMMENT '浏览次数',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_document_id` (`document_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文档基本属性';

-- ----------------------------
-- Table structure for document_group
-- ----------------------------
DROP TABLE IF EXISTS `document_group`;
CREATE TABLE `document_group` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `parent_id` bigint(11) NOT NULL COMMENT '父类id，0表示一级',
  `category_id` bigint(11) NOT NULL COMMENT '分类id',
  `order_id` int(2) NOT NULL DEFAULT '0' COMMENT '展示的顺序，从0开始',
  `title` varchar(100) NOT NULL COMMENT '标题',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `idx_document_id` (`document_id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_category_id` (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='树形文档';

-- ----------------------------
-- Table structure for oauth
-- ----------------------------
DROP TABLE IF EXISTS `oauth`;
CREATE TABLE `oauth` (
  `id` bigint(20) NOT NULL,
  `createdAt` bigint(20) NOT NULL,
  `expiresAt` bigint(20) NOT NULL,
  `updatedAt` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `version` bigint(20) NOT NULL,
  `authProviderId` varchar(32) NOT NULL,
  `authId` varchar(255) NOT NULL,
  `authToken` varchar(255) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `UNI_AUTH` (`authProviderId`,`authId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(11) NOT NULL COMMENT '用户id',
  `username` varchar(45) CHARACTER SET utf8 NOT NULL COMMENT '昵称',
  `email` varchar(45) NOT NULL COMMENT '邮箱(登录名)',
  `sex` char(2) DEFAULT 'M' COMMENT '用户性别',
  `avatar` varchar(255) NOT NULL DEFAULT 'aHR0cHM6Ly9hdmF0YXJzMS5naXRodWJ1c2VyY29udGVudC5jb20vdS8yMzQwMTY5MT9zPTQ2MCZ2PTQ=' COMMENT '头像',
  `description` varchar(150) DEFAULT '简单一句话，介绍你自己' COMMENT '简介',
  `github` varchar(100) DEFAULT NULL COMMENT 'github',
  `twitter` varchar(100) DEFAULT NULL COMMENT 'twitter',
  `weibo` varchar(100) DEFAULT NULL COMMENT 'weibo',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`) USING BTREE,
  UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户基本信息表';
