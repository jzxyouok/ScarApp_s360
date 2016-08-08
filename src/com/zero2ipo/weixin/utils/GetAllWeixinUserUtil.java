package com.zero2ipo.weixin.utils;

import com.zero2ipo.weixin.config.Config;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2015/7/9.
 */
public class GetAllWeixinUserUtil {
    /**获取所有关注的用户列表*/
    private final static String getUserUrl="https://api.weixin.qq.com/cgi-bin/user/get?access_token=";
    /**
     * 获取所有的关注用户
     * @return
     */
    public static List<String> getAllWeiXinUser() {
        String accessToken = GetAccessTokenUtil.getAccess_token2(Config.APPID,Config.SECRET);
        System.out.println("acc===="+accessToken);
        List<String> openIds = new ArrayList<String>();
        // 上传文件请求路径
        String action = getUserUrl+accessToken;
        try {
            URL urlGet = new URL(action);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String result = new String(jsonBytes, "UTF-8");
            JSONObject jsonObj = new JSONObject(result);
            System.out.println("users" + jsonObj.get("data"));
            JSONObject json1 = new JSONObject(jsonObj.get("data").toString());
            System.out.println(json1.toString());
            JSONArray json2 = new JSONArray(json1.get("openid").toString());
            System.out.println("关注个数======"+json2.length());
            for (int i = 0; i < json2.length(); i++) {
                openIds.add(i, json2.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return openIds;
    }
    public static  void main(String args[]){
        getAllWeiXinUser();
    }
}
