package com.ss.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;


@Data
@Getter
@Setter
@TableName("upload_log")
public class UploadLog {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Date CreateTime;
    private String uploadContent;
}
