/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50650 (5.6.50-log)
 Source Host           : localhost:3306
 Source Schema         : ss_price

 Target Server Type    : MySQL
 Target Server Version : 50650 (5.6.50-log)
 File Encoding         : 65001

 Date: 21/02/2025 11:55:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_items
-- ----------------------------
DROP TABLE IF EXISTS `config_items`;
CREATE TABLE `config_items`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_type_id` int(11) NOT NULL,
  `alias` varchar(155) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

SET FOREIGN_KEY_CHECKS = 1;
