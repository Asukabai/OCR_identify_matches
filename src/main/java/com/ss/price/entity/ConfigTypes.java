package com.ss.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@TableName("config_types")
public class ConfigTypes {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String typeName;
}
