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
    private String fileName; // 文件名
    private String productName; // 产品名称
    private String model; // 型号
    private String unitPrice;  // 单价
    private String manufacturer;  // 生产厂商
    private String contactPerson;  // 联系人
    private String phone;  // 联系方式
    private String purchaseTime; // 购买时间
}
