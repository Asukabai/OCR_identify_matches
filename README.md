## 项目简介

基于OCR技术的文本录入信息识别匹配系统，目前使用场景是识别供应商产品文件信息，并根据一定的匹配提取逻辑，最终集成了信息展示，统计和管理等功能的一个Web应用系统。

## 使用技术栈

使用了Java的springboot框架，调用了离线版和在线版两种开源OCR接口，实现了前后端分离版本的OCR文本识别与信息内容匹配的功能，并附有前端页面的搜索查询和统计数据的功能。
（并提供了一个工具类，安装对应环境之后，可以直接使用本项目下的Python工具类进行运行即可识别并将结果导出到Excel）

## 项目架构图

![image](https://github.com/user-attachments/assets/460973a1-6bf2-4bdb-9adf-2e5ca9e0e510)

## 项目初始化

### 创建 Git
echo "# OCR_identify_matches" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M master
git remote add origin https://github.com/Asukabai/OCR_identify_matches.git
git push -u origin master
git pull（更新拉取项目）
![image](https://github.com/user-attachments/assets/bee5833f-d8e5-42ad-bd20-bc67b3686d86)

