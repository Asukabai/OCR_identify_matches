//package com.ss.price.utils;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.Header;
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
///**复制的钉钉免登陆工具类
// *          loginserviceImpol -> 获取accessToken使用
// * 2023-3-15*/
//@Slf4j
//public class HttpHelper {
//
//    @Value("${azure.corpid}")
//    private static String corpid;
//
//    @Value("${azure.appsecret}")
//    private static String appsecret;
//
//    @Value("${azure.agentid}")
//    private static String agentid;
//
//    @Value("${azure.appkey}")
//    private static String appkey;
//
//    public static JSONObject httpGet(String url) {
//        //创建httpClient
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(url);                             //生成一个请求
//        RequestConfig requestConfig = RequestConfig.custom().         //配置请求的一些属性
//                setSocketTimeout(2000).setConnectTimeout(2000).build();
//        httpGet.setConfig(requestConfig);                             //为请求设置属性
//        CloseableHttpResponse response = null;
//        try {
//            response = httpClient.execute(httpGet);
//            //如果返回结果的code不等于200，说明出错了
//            if (response.getStatusLine().getStatusCode() != 200) {
//                return null;
//            }
//            HttpEntity entity = response.getEntity();                 //reponse返回的数据在entity中
//            if (entity != null) {
//                String resultStr = EntityUtils.toString(entity, "utf-8");//将数据转化为string格式
//                JSONObject result = JSONObject.parseObject(resultStr);  //将结果转化为json格式
//                if (result.getInteger("errcode") == 0) {                  //如果返回值得errcode值为0，则成功
//                    //移除一些没用的元素
//                    result.remove("errcode");
//                    result.remove("errmsg");
//                    return result;                                    //返回有用的信息
//                } else {                                                 //返回结果出错了，则打印出来
//                    int errCode = result.getInteger("errcode");
//                    String errMsg = result.getString("errmsg");
//                    throw new Exception("ErrorCode:" + errCode + "ErrorMsg" + errMsg);
//                }
//            }
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (response != null) try {
//                response.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//    // 调用httpGet方法获得access_token的代码实现：
//    public static String getAccess_Token(String appkey, String appsecret) {
//        String url = "https://oapi.dingtalk.com/gettoken?" + "appkey=" + appkey + "&appsecret=" + appsecret;
//        JSONObject res = HttpHelper.httpGet(url);                      //将httpGet方法封装在HttpHelper类中
//        String access_token = "";
//        if (res != null) {
//            access_token = res.getString("access_token");
//        } else {
//            new Exception("获取token异常");
//        }
//        return access_token;
//    }
//
//    // 执行POST请求（如果免登录成功后不需要推送钉钉消息，则不需要此方法）
//    public static String httpPost(String url, Map<String, Object> params, String encoding) throws Exception {
//        String result = "";
//        // 创建默认的httpClient实例.
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        // 创建httppost
//        HttpPost httppost = new HttpPost(url);
//        //参数
//        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        if (params != null) {
//            Set<String> keys = params.keySet();
//            for (String key : keys) {
//                formparams.add(new BasicNameValuePair(key, params.get(key).toString()));
//            }
//        }
//        UrlEncodedFormEntity uefEntity;
//        try {
//            uefEntity = new UrlEncodedFormEntity(formparams, encoding);
//            httppost.setEntity(uefEntity);
//            CloseableHttpResponse response = httpclient.execute(httppost);
//            try {
//                Header[] headers = response.getAllHeaders();
//                for (Header header : headers) {
//                    log.debug(header.getName() + "-->" + header.getValue());
//                }
//                HttpEntity entity = response.getEntity();
//                if (entity != null) {
//                    result = EntityUtils.toString(entity, encoding);
//                }
//            } finally {
//                response.close();
//            }
//        } catch (IOException e) {
//            throw e;
//        } finally {
//            // 关闭连接,释放资源
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//            }
//        }
//        return result;
//    }
//}