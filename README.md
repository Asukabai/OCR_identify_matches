## 项目简介

🌈基于OCR技术的通用文本录入信息识别匹配系统，🙇 目前使用场景是识别供应商产品文件信息，并根据一定的匹配提取逻辑，最终集成了信息展示，统计和管理等功能的一个Web应用系统。

## 项目技术栈

使用了Java的springboot框架，调用了离线版和在线版两种开源OCR接口，实现了前后端分离版本的OCR文本识别与信息内容匹配的功能，并附有前端页面的搜索查询和统计数据的功能。
（并提供了一个工具类，安装对应环境之后，可以直接使用本项目下的Python工具类进行运行即可识别并将结果导出到Excel）

## 项目架构图（UML）

![image](https://github.com/user-attachments/assets/460973a1-6bf2-4bdb-9adf-2e5ca9e0e510)

## 项目初始化

### 引入 Git
git init
git add README.md
git commit -m "first commit"
git branch -M master
git remote add origin https://github.com/Asukabai/OCR_identify_matches.git
git push -u origin master
git pull  
![image](https://github.com/user-attachments/assets/bee5833f-d8e5-42ad-bd20-bc67b3686d86)

### 初始化 SpringBoot 框架
maven 3.6.5
MySQL 5.6.5
jdk 1.8 + mybatis-plus 3.5.6

## 功能介绍和库表设计

### 1.0 版本主要功能
识别整个图片的所有信息，数据库存储图片信息，对象单位是图片，搜索也是按照识别图片内容进行模糊搜索

### 1.0 版本库表设计

### 2.0 版本主要功能
将识别内容细分，由于图片中可能包含多个产品信息，所以将识别内容细分为多个对象，对象单位是产品信息，搜索也是按照识别到的产品信息进行模糊搜索

### 2.0 版本库表设计

产品名称 单价 型号，厂家（乙方） 联系人 电话 采购时间（精确到年月）

CREATE TABLE `product_info` (
`id` bigint(20) NOT NULL AUTO_INCREMENT,
`file_id` bigint(20) NOT NULL,
`product_name` varchar(255) DEFAULT NULL,
`model` varchar(100) DEFAULT NULL,
`unit_price` varchar(50) DEFAULT NULL,
`manufacturer` varchar(255) DEFAULT NULL,
`contact_person` varchar(100) DEFAULT NULL,
`phone` varchar(20) DEFAULT NULL,
`purchase_time` varchar(100) DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;