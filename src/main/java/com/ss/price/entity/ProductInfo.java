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
@TableName("product_info")
public class ProductInfo {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long fileId;
    private String productName;
    private String model;
    private String unitPrice;
    private String manufacturer;
    private String contactPerson;
    private String phone;
    private String purchaseTime;
}
