package com.ss.price.utils;


import okhttp3.*;
import org.json.JSONObject;


import java.io.*;


/**
 * 获取百度表格识别的AccessToken
 *
 * 需要添加依赖
 * <!-- https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp -->
 * <dependency>
 *     <groupId>com.squareup.okhttp3</groupId>
 *     <artifactId>okhttp</artifactId>
 *     <version>4.12.0</version>
 * </dependency>
 */

class Sample {

    // eu9IasMzMRvDADcFyivYUlIC
    public static final String API_KEY = "eu9IasMzMRvDADcFyivYUlIC";

    // 3XIT9pwGK8xtC5VgfLbTSZe4d5g5cm8P
    public static final String SECRET_KEY = "3XIT9pwGK8xtC5VgfLbTSZe4d5g5cm8P";

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "cell_contents=false&return_excel=false");
        Request request = new Request.Builder()
            .url("https://aip.baidubce.com/rest/2.0/ocr/v1/table?access_token=" + getAccessToken())
            .method("POST", body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Accept", "application/json")
            .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }
    
    
    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     * 引入依赖：
     * 		<dependency>
     * 			<groupId>org.json</groupId>
     * 			<artifactId>json</artifactId>
     * 			<version>20210307</version>
     * 		</dependency>
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
}