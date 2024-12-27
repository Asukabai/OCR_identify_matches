package com.ss.price.entity.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class RequestPack<T> implements Serializable {

    private static final long serialVersionUID = -2623121345126165845L;

    // 你需要根据实际情况设置你的密钥
    private static final String SECRET = "your_secret_key";

    private String reqID;
    private String method;
    private String sender;
    private String sendee;
    private String token;
    private T reqData;

    public boolean isTokenOK(String token) {
//        try {
//            // 使用密钥创建 JWTVerifier 对象
//            Algorithm algorithm = Algorithm.HMAC256(SECRET);
//            JWTVerifier verifier = JWT.require(algorithm).build();
//            // 使用 JWTVerifier 对象验证 token
//            DecodedJWT decodedJWT = verifier.verify(token);
//            // 如果没有抛出异常，则表示 token 验证成功
//            return true;
//        } catch (Exception e) {
//            // 如果抛出异常，则表示 token 验证失败
//            return false;
//        }
        return true;
    }
}