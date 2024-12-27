package com.ss.price.utils;

import com.ss.price.entity.code.ResultCode;
import com.ss.price.entity.dto.RespondDto;

public class RespondUtils {

    /**
     * 创建成功响应的 RespondDto 对象
     * @param msg 响应消息体
     * @return 成功响应的 RespondDto 对象
     */
    public static <T> RespondDto<T> success(String msg) {
        return new RespondDto<>(ResultCode.OK, msg);
    }


    /**
     * 创建成功响应的 RespondDto 对象
     * @param <T> 数据类型
     * @param data 响应数据
     * @param message 响应消息
     * @return 成功响应的 RespondDto 对象
     */
    public static <T> RespondDto<T> success(T data, String message) {
        return new RespondDto<>(ResultCode.OK, message, data);
    }

    /**
     * 创建失败响应的 RespondDto 对象
     * @param <T> 数据类型
     * @param message 错误消息
     * @return 失败响应的 RespondDto 对象
     */
    public static <T> RespondDto<T> error(String message) {
        return new RespondDto<>(ResultCode.ERROR, message, null);
    }

    public static <T> RespondDto<T> error1(T data, String message) {
        return new RespondDto<>(ResultCode.ERROR, message,data);
    }
}