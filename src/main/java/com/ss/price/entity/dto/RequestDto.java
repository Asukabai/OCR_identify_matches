package com.ss.price.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**请求实体类
@author zhangxuejin*/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto<T> implements Serializable {

    /**token*/
    private String token;
    /**请求id*/
    private Long reqId;
    /**时间戳*/
    private long timeStamp;
    /**发送方*/
    private String sender;
    /**接收方*/
    private String sendee;
    /**消息体*/
    private String msg;
    /**请求参数*/
    private T reqData;
}
