## 项目简介
🌈基于OCR技术的通用文本信息自动识别匹配系统，🙇 支持识别用户上传（PDF或图片）文件信息，并根据要求提取的信息为依据，以一定的逻辑匹配提取，最终集成了信息展示，统计和管理等功能的一个Web应用系统。

## 项目技术栈
使用了Java的springboot框架，调用了离线版和在线版两种开源OCR接口，实现了前后端分离版本的OCR文本识别与信息内容匹配的功能，并附有前端页面的搜索查询和统计数据的功能。
（并提供了一个工具类，安装对应环境之后，可以直接使用本项目下的Python工具类进行运行即可识别并将结果导出到Excel）

## 项目架构图（UML）

![image](https://github.com/user-attachments/assets/460973a1-6bf2-4bdb-9adf-2e5ca9e0e510)

## 项目初始化

### 引入 Git

```git init && ```
```git add README.md  ```
```git commit -m "first commit" ```
```git branch -M master ```
```git remote add origin https://github.com/Asukabai/OCR_identify_matches.git ```
```git push -u origin master ```
```git pull ```
### 初始化 SpringBoot 框架
<blockquote>
maven 3.6.5
<br>
MySQL 5.6.5
<br>
jdk 1.8 + mybatis-plus 3.5.6
</blockquote>

## 功能介绍和库表设计

### 1.0 版本主要功能
用户上传文件，然后将文件转化成图片，通过识别并存储图片对应的图片信息数据，对象单位是图片，搜索也是按照识别图片内容进行模糊搜索
<br><br> <!-- 插入两个换行符，增加文字与图片之间的空白 -->
![image](https://github.com/user-attachments/assets/34f05747-ee82-4715-9df9-1eb6b512beca)
<br><br> <!-- 插入两个换行符，增加文字与图片之间的空白 -->
![image](https://github.com/user-attachments/assets/423d39b0-a4b7-4da5-ae7d-faf8d8819113)
<br> <!-- 插入两个换行符，增加文字与图片之间的空白 -->
### 1.0 版本库表设计（适用于对整体内容进行搜索查看预览的情况）

file_info 表

| 字段名           | 类型           | 描述                               |
| ---------------- | -------------- | ---------------------------------- |
| `id`             | bigint(20)      | 文件的唯一标识符，主键，自动递增    |
| `file_name`      | varchar(255)    | 文件名，不能为空                   |
| `content`        | text           | 文件内容，文本类型，可以为空       |
| `image_id`       | bigint(20)      | 图片的唯一标识符，外键，默认为 NULL |
| `create_time`    | datetime        | 文件创建时间，默认当前时间         |
| `update_time`    | datetime        | 文件更新时间，自动更新为当前时间  |

file_image 表

| 字段名           | 类型           | 描述                               |
| ---------------- | -------------- | ---------------------------------- |
| `id`             | bigint(20)      | 图片的唯一标识符，主键，自动递增    |
| `image_id`       | bigint(20)      | 图片的标识符，可以为空              |
| `image_url`      | varchar(255)    | 图片的URL地址，不能为空            |
| `create_time`    | datetime        | 图片创建时间，默认当前时间         |
| `update_time`    | datetime        | 图片更新时间，自动更新为当前时间  |

### 2.0 版本主要功能（1.0 升级版，适用于定制化对整体内容进行解析配置搜索查看预览）
优化信息展示内容，将识别内容细分。由于每张图片中可能包含多个产品信息，所以将识别内容细分为多个对象，对象单位是产品信息，搜索也是按照识别到的产品信息进行模糊搜索。

### 2.0 版本库表设计

#### 产品信息表 `product_info`
在原有的基础上更新表结构，该表用于存储识别出的产品信息，包含产品的基本信息以及采购相关内容。

| 字段名           | 类型           | 描述                   |
| ---------------- | -------------- | ---------------------- |
| `id`             | bigint(20)      | 主键，自增              |
| `file_id`        | bigint(20)      | 文件ID，关联上传文件   |
| `product_name`   | varchar(255)    | 产品名称               |
| `model`          | varchar(100)    | 产品型号               |
| `unit_price`     | varchar(50)     | 产品单价               |
| `manufacturer`   | varchar(255)    | 厂家（乙方）           |
| `contact_person` | varchar(100)    | 联系人                 |
| `phone`          | varchar(20)     | 电话                   |
| `purchase_time`  | varchar(100)    | 采购时间（精确到年月） |
