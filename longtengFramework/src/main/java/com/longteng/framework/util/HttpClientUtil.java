package com.longteng.framework.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

public class HttpClientUtil {

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);

    // 返回两个信息,状态码和返回结果
    @SuppressWarnings("unchecked")
    public static Map<String, String> request(Map<String, String> paramMap, Map<String, String> headers) {

        Map<String, String> returnMap = new HashMap<String, String>();
        String url = paramMap.get("url");
        String method = paramMap.get("method");
        if (StringUtil.isEmpty(url)) {
            returnMap.put("responseBody", "url不能为空!");
            return returnMap;
        }
        if (StringUtil.isEmpty(method)) {
            returnMap.put("responseBody", "method不能为空!");
            return returnMap;
        }

        String requestBody = paramMap.get("requestBody");
        String contentType = paramMap.get("Content-Type");

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;

        if ("get".equalsIgnoreCase(method)) {
            HttpGet get = new HttpGet(url);
            try {
                response = client.execute(get);
                String resCode = String.valueOf(response.getStatusLine().getStatusCode());
                logger.info("返回码：" + resCode);
                if (resCode.startsWith("2")) {
                    String entityString = EntityUtils.toString(response.getEntity(), "UTF-8");
                    logger.info("返回报文：" + entityString);
                    returnMap.put("responseBody", entityString);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                close(client, response);
            }
        }

        if ("post".equalsIgnoreCase(method)) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", contentType);

            if ("application/json".equalsIgnoreCase(contentType)) {
                try {

                    StringEntity entity = new StringEntity(requestBody);
                    post.setEntity(entity);

                    response = client.execute(post);
                    String resCode = String.valueOf(response.getStatusLine().getStatusCode());
                    logger.info("返回码：" + resCode);

                    if (resCode.startsWith("2")) {
                        String entityString = EntityUtils.toString(response.getEntity(), "UTF-8");
                        logger.info("返回报文：" + entityString);
                        returnMap.put("responseBody", entityString);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    close(client, response);
                }
            }

            if ("application/x-www-form-urlencoded".equalsIgnoreCase(contentType)) {

                Map<String, String> reqMap = (Map<String, String>) JSON.parse(requestBody);

                List<NameValuePair> pairList = new ArrayList<NameValuePair>();

                try {

                    for (String key : reqMap.keySet()) {
                        String value = reqMap.get(key);
                        BasicNameValuePair pair = new BasicNameValuePair(key, value);
                        pairList.add(pair);
                    }

                    post.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));

                    response = client.execute(post);

                    String resCode = String.valueOf(response.getStatusLine().getStatusCode());
                    logger.info("返回码：" + resCode);

                    if (resCode.startsWith("2")) {
                        String entityString = EntityUtils.toString(response.getEntity());
                        logger.info("返回报文：" + entityString);
                        returnMap.put("responseBody", entityString);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

        return returnMap;

    }

    private static void close(CloseableHttpClient client, CloseableHttpResponse response) {

        if (response != null) {
            try {
                EntityUtils.consume(response.getEntity());
                response.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (client != null) {
            try {
                client.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
