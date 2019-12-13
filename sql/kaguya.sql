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

 Date: 13/12/2019 12:44:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 778445761024000, 'Java', 0);
INSERT INTO `category` VALUES (2, 783999891935232, '设计模式', 1);
INSERT INTO `category` VALUES (3, 784000026152960, 'Spring', 2);
INSERT INTO `category` VALUES (4, 784000227479552, '数据结构', 3);

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document`  (
  `tid` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `document_id` bigint(11) NOT NULL COMMENT '文档id',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NULL COMMENT '更新时间',
  `create_user` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'System' COMMENT '创建人',
  `update_user` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `views` int(11) NULL DEFAULT 0 COMMENT '浏览次数',
  PRIMARY KEY (`tid`) USING BTREE,
  UNIQUE INDEX `uk_document_id`(`document_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档基本属性' ROW_FORMAT = Dynamic;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '树形文档' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
