package com.ss.price.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(C),2023,陕西晟思智能测控有限公司
 * FileName: RsaUtils
 * Author: ZhangXueJin
 * Date:   2024/9/8/032:27
 * Description: 非对称加解密 工具类
 * History:
 * <author>      <time>     <version>     <desc>
 * 作者姓名       修改时间      版本号         描述
 */

@Slf4j
@Component
public class RsaUtils {
//    密钥长度
    private static final Integer KEY_SIZE =2048;

    /**生成公私钥*/
    public static Map<String, String> createKey(int keySize) {

        HashMap<String, String> keyMap = new HashMap<>();
//       keyPairGenerator 密钥对生成器        keyPair 生成密钥对
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(KEY_SIZE);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            keyMap.put("publicKey", Base64.encodeBase64String(publicKey.getEncoded()));
            keyMap.put("privateKey", Base64.encodeBase64String(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.info("【生成公私钥失败：】"+e.getMessage());
        }
        return keyMap;
    }

    /**私钥解密
     * @param cipherText;密文
     * @param ;privateKey ;私钥*/
//    public String priKeyDecode(String cipherText,String privateKey){
    public static String priKeyDecode(String cipherText){
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSS56j/o9j6tj2byR37gRM7/yLqDveu5T8ATqdLipA4Uc3cYQtP+IKiFQW/3XlfmWRiZQ7g9P/0Ab8UQ1DwLPLS5HmDZ68OQjw+ASep3Ha+9A77KfA80pr4LHPJauDsfiS3gICkztqjy3VgQuIlq/0rQn9uRPRc7k0OqdnbMON1VsQWYRxIBvbJTZGdpdUqkrEvBGSyR47rx0ENtJDsTxAcAK4ji5wFZ7b3weei0blAiXquU8jtpUciiVdoFEtG3tED+z8wuwiyseHF27m4cMjQbJbyK677Ls+QiUMbeoF0gx325AqtJqfzKkdoiZnLB5u0CrwNhfYo9aLFXRaktkdAgMBAAECggEACJJluTYxMKmSDN52RJCtr9Rf987jsylKkXmA9bS+xQ5uHdGI7R3RIMzcIHzXa5r8WIq3JMWUpmiKAD6/iDjCUCcdZV1m76Bn5baijqlBApO+n8Q+EBAxTCDeSkTqx8ylw/U+KOh3ksbQ6LGm+1nY9X0uGK+/eYZOU8EcDfNRgf3nBd7hYhf1YgDVmTCqUt/4U2KHDltLL5E2GF1TQ6tP70tNT6M6ASRoh31t3xmUaZSNtiFjivgV3W7u2Phmx33r7puKADt7AKWkK2pa4J3d/f7ZLc90wxLtg3FIXmyOzR90piGVQS2YkSOaIGyU8C77Lql6o8FC7XOADyJpJ7VkAQKBgQD1ZN7oEnG2w3izlu01mFjSRhQhlfB5LH7giyyUWQWmMm/kWKIjGjhuOZz9/MxsqS/sKOM0yw5qv64EKNtYuYILbaRj5sRCtOO5ESfxmo6exh7Fv5vhPAKnNiq+fT6IHTAro1uj9plDkrjBdMSiV/jt5INgSj+HxW7UU0TPuMG/ewKBgQCYnkkPgUIpwv4jFYThf+YOq8yTIPTPAH7KxIuZlW6Lv2J6cgUAYKLx+VPQQ2HRfVtW6VzsuPLGy3kteq+nZFpGeA4pXimla7nvdPeabvEZ+RSUTKp/dt5ORgnQLco77hT1ynv2r6Zsd//0MPOO8RJukGOjXbP75Olw+cifsNjaRwKBgHL+NPK9ieoiSZKm3DvsS1smGdLvR4QRFNe2gEp4OQx7wDFaU4hthhopB2eCKm2VDu707hvUN8pQToSGfNtj+wvN8mYaQsgbqkcUQgqW6dR/dPYn5y0yCPBAkuE3IbF5NBSO9aetLH8c51VImciVvwIDvojF4mWGYHxBFRLeOnS3AoGAFJad0oqka0/TytXXVDhVx7rjYSG8BMTTVfVHw9NF03JhDZWeGRvwP2ZcKSc95e3cSZA3qfOVRL4kPb9kPNer2f/OIUSDp74tmiEUWfAHdHKGlS72fTWC7dpC7qOOuaD3kOTc7j1p3Q4fKEkGQk1BKEswNdSpOBz8ZPI9X1WTG3cCgYEAuY3HeQHDQDa6l1OjkIx7IPoHeFratN0ZzhtWEDEgGwEQsHRWIofJ3CmqJAPW03aT8T2b3MVguF8WjrEpjSlWRbHBWO0k7YelcajAUThLze26TPsX1+Eod25OZOBTR3jA1zr6Mvmp2M7ETVZ+4oiEFbrxXEanGuE0TkYVQdGHpfA=\n";

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));

        try {
            KeyFactory rsa = KeyFactory.getInstance("RSA");
            PrivateKey aPrivate = rsa.generatePrivate(pkcs8EncodedKeySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE,aPrivate);
            byte[] bytes = cipher.doFinal(Base64.decodeBase64(cipherText));
//            System.out.println(new String(bytes));
            return new String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("解密失败出现异常");
            return null;
        }
    }

    public static void main(String[] args) {
        Map<String, String> key = createKey(KEY_SIZE);
        for (Map.Entry<String, String> stringStringEntry : key.entrySet()) {
            System.out.println(stringStringEntry);
        }
        priKeyDecode("LNTLykbLFxlSaft4JSUu5vBTCpJjq6PumvSB/hw8dPhgsYtShFuX+hKmToLqymmPu2P29MMocaPexIgHzbU7Vq63GgoFwGn7VYVroj9WjRlq0QD71mEtr+eEy2kbg7pkBXGqDZ1Zh8N9+xwsHt/UvZulQZHAESoZr6EXmhm1lP9Nz0pPNDqTeSh7RboxbsObvYuLg3PodDMwrrp0RE2hOsndRLsAiAWeS9X+ehI0ZY1tpSvUyRy5RGDJnRIhT4XRPemUdST9JfYZ8b19GT9afXihRmdx/ue4+gDuMA9Z7s66br25ytcG30Q2Odg/X+tcaq8FhiRdP7Bm0El4MVrnHA==");
    }
}
