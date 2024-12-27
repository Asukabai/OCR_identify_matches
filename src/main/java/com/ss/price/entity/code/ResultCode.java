package com.ss.price.entity.code;

/**响应码
* @author zhangxuejin*/
public class ResultCode {
    /**空数据*/
    public static final int ERROR = 0;
    /**成功*/
    public static final int OK = 1;
    /**D90已拆除*/
    public static final int D90 = 2;
    /**其他错误*/
    public static final int OTHER_ERROR = -1;
    /**用户密码错误*/
    public static final int USER_PASSWORD_ERROR = -100;
    /**用户不存在*/
    public static final int USER_NONE = -101;
    /**请求过快*/
    public static final int REQUEST_TO0_FAST = -102;
    /**ip访问受限*/
    public static final int IP_VISIT_CANt = -103;
    /**请求类型错误*/
    public static final int REQUEST_TYPE_ERROR = -104;
    /**未知异常*/
    public static final int UNKNOWN_ABNORMAL = -9999;


}
