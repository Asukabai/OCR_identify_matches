package com.ss.price.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


/**响应实体类
 * @author zhangxuejin*/

@Data
@AllArgsConstructor
public class RespondDto<T> implements Serializable {

    private static final long serialVersionUID = 3179841517773335364L;

    /**时间戳*/
    private long timeStamp;
    /**响应码*/
    private Integer result;
    /**请求ID*/
    private Long reqID;
    /**响应ID*/
    private Long respID;
    /**发送方*/
    private String sender;
    /**接收方*/
    private String sendee;
    /**消息体*/
    private  String msg;
    /**响应数据*/
    private T respData;

    /**
     * 没有数据返回，人为指定 响应码、消息体
     * @param result
     * @param msg
     */
    public RespondDto(Integer result, String msg){

        this.timeStamp = System.currentTimeMillis();
        this.result = result;
        this.msg = msg;
        this.sendee ="";
        this.sender ="";
        this.respID = 0L;
        this.reqID = 0L;

    }

    /**
     * 有数据返回，人为指定 响应码、消息体
     * @param msg
     * @param result
     * @param respData
     */
    public RespondDto(Integer result, String msg, T respData){

        this.timeStamp = System.currentTimeMillis();
        this.result = result;
        this.msg = msg;
        this.sendee ="";
        this.sender ="";
        this.respID = 0L;
        this.reqID = 0L;
        this.respData = respData;
    }
}
