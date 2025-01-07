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
import java.util.List;

@Data
@Getter
@Setter
@TableName("file_info")
public class FileInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String fileName;
    private String wordContent;
    private String tableContent;
    private Long imageId;
    private Date createTime;
    private Date updateTime;

    @TableField(exist = false)
    private List<FileImage> fileImages;
}
