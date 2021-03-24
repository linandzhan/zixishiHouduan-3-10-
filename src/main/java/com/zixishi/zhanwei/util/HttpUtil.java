package com.zixishi.zhanwei.util;

import com.google.gson.Gson;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 发送http请求的工具类，包括普通的GET POST请求
 */
public class HttpUtil {

    private final static String CHARSET_DEFAULT = "UTF-8";
    private static Logger record = LoggerFactory.getLogger("record");
    private static Boolean isRecord = false;


    public static HttpResult doGet(String baseUrl, Map<String, String> paramsMap){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpResult result = new HttpResult();
//        for (Map.Entry<String, String> entry:paramsMap.entrySet()
//             ) {
//            baseUrl+=entry.getKey()+"="+entry.getValue()+"&";
//        }
//        baseUrl=baseUrl.substring(0,baseUrl.length()-1);
        record.info("url:{}", baseUrl);
        HttpGet httpGet = new HttpGet(baseUrl);
        try {
            response=httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity(), CHARSET_DEFAULT);
            int statusCode = response.getStatusLine().getStatusCode();
            if (isRecord) {
                record.info("{}|{}|{}", baseUrl, statusCode, body);
            }
            result.setStatus(statusCode);
            result.setBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * post请求  编码格式默认UTF-8
     *
     * @param url 请求url
     * @return
     */
    public static HttpResult doPost(String url, Object obj) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse resp = null;

        HttpResult result = new HttpResult();
        try {
            Map<String, String> params = objectToMap(obj);
            HttpPost httpPost = new HttpPost(url);
            if (params != null && params.size() > 0) {
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET_DEFAULT));
            }

            resp = httpClient.execute(httpPost);
            String body = EntityUtils.toString(resp.getEntity(), CHARSET_DEFAULT);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (isRecord) {
                record.info("{}|{}|{}|{}", url, new Gson().toJson(obj), statusCode, body);
            }
            result.setStatus(statusCode);
            result.setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resp) {
                try {
                    resp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static HttpResult weiMengDoPost(String url, String content) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse resp = null;

        HttpResult result = new HttpResult();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setHeader("x-Authority-Identity","MET99SS");
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("", content));
            httpPost.setEntity(new UrlEncodedFormEntity(list, CHARSET_DEFAULT));
            resp = httpClient.execute(httpPost);
            String body = EntityUtils.toString(resp.getEntity(), CHARSET_DEFAULT);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (isRecord) {
                record.info("{}|{}|{}", url, statusCode, body);
            }
            result.setStatus(statusCode);
            result.setBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resp) {
                try {
                    resp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMap(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String fieldValue = "";
            if (field.getType() == String.class || field.getType() == Integer.class || field.getType() == int.class) {
                fieldValue = field.get(obj) == null ? "" : field.get(obj).toString();
            } else {
                fieldValue = new Gson().toJson(field.get(obj));
            }
            map.put(fieldName, fieldValue);
        }
        return map;
    }


    public static void main(String[] args) {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url("https://api.gugudata.com/barcode/qrcode?appkey=BLPTTYNNCYA4&content=http://www.baidu.com&size=500")
//                .method("GET", null)
//                .build();
//        Response response = client.newCall(request).execute();
        HttpResult httpResult = doGet("https://api.gugudata.com/barcode/qrcode?appkey=BLPTTYNNCYA4&content=http://192.168.0.47:8088/#/login&size=500", null);
        String body = httpResult.getBody();

        System.out.println(body);
    }
}
