package com.ss.price.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author 32937
 */
@Data
public class LoginVo implements Serializable {
    private static final long serialVersionUID = 3704057655632018816L;
    private String name;
    private BigInteger userIndex;
    private String token;
    private Integer auth;
}