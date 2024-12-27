package com.ss.price.entity.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class RequestPackCheak implements Serializable {

    private static final long serialVersionUID = -3995196912471231629L;

    private String productId;
    private String selectedCategory;
    private String storedProductPerson;
    private String productName;
    private String productType;
}
