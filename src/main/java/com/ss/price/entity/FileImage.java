package com.ss.price.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
@TableName("file_image")
public class FileImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long imageId;
    private String imageUrl;

    private Date createTime;
    private Date updateTime;
}
