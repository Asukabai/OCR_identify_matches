package com.ss.price.entity.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class ReqData  implements Serializable {


    private static final long serialVersionUID = -6429446418923371411L;

    private String code;

    // 添加一个带有布尔参数的构造函数
    public ReqData(boolean dummy) {
        // 这里可以留空，或者设置默认值
    }
}
