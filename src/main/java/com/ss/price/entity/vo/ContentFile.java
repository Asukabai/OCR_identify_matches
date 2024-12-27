package com.ss.price.entity.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ContentFile {

    private String name;//文件名
    private String content;// base64
    private String type; // 图片或者视频
}
