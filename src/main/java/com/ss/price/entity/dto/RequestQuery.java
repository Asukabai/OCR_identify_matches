package com.ss.price.entity.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestQuery {


    private String query;
    private int pageNum;
    private int pageSize;
}