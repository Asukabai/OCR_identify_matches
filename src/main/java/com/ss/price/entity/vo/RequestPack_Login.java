package com.ss.price.entity.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class RequestPack_Login implements Serializable {


    private static final long serialVersionUID = -1943409843430401851L;
    private String code;

}