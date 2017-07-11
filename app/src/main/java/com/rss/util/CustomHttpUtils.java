package com.rss.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/5/17.
 */
public class CustomHttpUtils {

    public static String FINAL_REQ_URL = "";

    /**
     * get 方式获取Reader
     *
     * @param interfaceUrl
     * @return
     */
    public static BufferedReader httpGETReader(String interfaceUrl) {
        BufferedReader br = null;
        try {
            FINAL_REQ_URL = interfaceUrl;
            URL url = new URL(interfaceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else if (connection.getResponseCode() ==301 || connection.getResponseCode() == 302) {
                connection.disconnect();
                br = httpGETAssistantReader(interfaceUrl);
            }
//            if (null != br) {
//                br.close();// 关闭流
//            }
            connection.disconnect();// 断开连接
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }

    /**
     * 301执行
     * 可能http变更为https
     *
     * @param interfaceUrl
     * @return
     */
    public static BufferedReader httpGETAssistantReader(String interfaceUrl) {
        BufferedReader br = null;
        try {
            //执行替换
            interfaceUrl = interfaceUrl.replace("http:", "https:");
            FINAL_REQ_URL = interfaceUrl;
            URL url = new URL(interfaceUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            if (null != br) {
                br.close();
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
}
