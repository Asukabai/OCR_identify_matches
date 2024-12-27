package com.ss.price.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.Date;
import java.util.List;

// 定义一个 DTO 类用于返回数据

/**
 * @author zhangxuejin
 */
@Data
public class CardRecordDto {

    private Long id;
    private String productId;
    private String currentLocation;
    private String description;
    private String operation;
    private List<String> fileUrls; // 新增字段：图片或视频的URL列表
    private List<String> types; // 新增字段：图片或视频的类型列表
    private String fileName;
    private String productType;
    private Integer count;
    private String belongContent;    // 归属项目

    /**
     * 使用 yyyy-MM-dd HH:mm:ss 格式，并指定时区为中国上海时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    // 省略 getter 和 setter 方法
}