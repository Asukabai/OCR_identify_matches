package com.ss.price.utils;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestUtil {
    private static volatile RestUtil restUtil;

    private RestUtil() {
    }

    public static RestUtil getSingleton() {
        if (restUtil == null) {
            Class var0 = RestUtil.class;
            synchronized(RestUtil.class) {
                if (restUtil == null) {
                    restUtil = new RestUtil();
                }
            }
        }

        return restUtil;
    }

    public JsonObject DO_POST(String url, JsonObject json) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        JsonObject response = null;

        try {
            StringEntity s = new StringEntity(json.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            HttpResponse res = client.execute(post);
            if (res.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = res.getEntity();
                String result = EntityUtils.toString(res.getEntity());
                response = GsonUtil.jsonToJsonObject(result);
            }

            return response;
        } catch (Exception var10) {
            throw new RuntimeException(var10);
        }
    }

    public String getMethod(String url) {
        BufferedReader br = null;

        try {
            HttpURLConnection con = initConnection(url, "get");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            if ((line = br.readLine()) != null) {
                String var5 = line;
                return var5;
            }
        } catch (IOException var16) {
            var16.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return null;
    }

    public String postMethod(String url, String query) {
        PrintStream ps = null;
        BufferedReader br = null;

        String var7;
        try {
            HttpURLConnection con = initConnection(url, "post");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            ps = new PrintStream(con.getOutputStream());
            ps.print(query);
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            if ((line = br.readLine()) == null) {
                return null;
            }

            var7 = line;
        } catch (IOException var18) {
            var18.printStackTrace();
            return null;
        } finally {
            try {
                ps.close();
                br.close();
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        return var7;
    }

    private static HttpURLConnection initConnection(String url, String type) {
        URL restURL = null;
        HttpURLConnection conn = null;

        try {
            restURL = new URL(url);
            conn = (HttpURLConnection)restURL.openConnection();
            conn.setRequestMethod(type.toUpperCase());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
        } catch (MalformedURLException var5) {
            var5.printStackTrace();
        } catch (ProtocolException var6) {
            var6.printStackTrace();
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        return conn;
    }

    public static void main(String[] args) {
        RestUtil restUtil = new RestUtil();

        try {
            String url = "http://localhost:8081/mm/radiosend";
            String query = "{\"taskId\":422,\"lat\":34.1342126,\"lng\":108.5694107,\"altitude\":456.0,\"mes\":{\"doseRate\":287.0,\"measureTime\":\"2019-03-21 14:35:17\",\"note1\":\"34.15651\",\"note2\":\"108.97742\",\"nuclide1\":\"155\",\"nuclide2\":\"154\",\"nuclide3\":\"157\",\"nuclide4\":\"152\",\"nuclide5\":\"159\"}}";
            restUtil.postMethod(url, query);
        } catch (Exception var4) {
            System.out.println(var4.getMessage());
        }

    }
}
