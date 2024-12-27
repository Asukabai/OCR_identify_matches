package com.ss.price.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@TableName("config_items")
public class ConfigItems {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long configTypeId;
    private String alias;
}
