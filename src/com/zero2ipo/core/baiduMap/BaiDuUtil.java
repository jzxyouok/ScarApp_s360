package com.zero2ipo.core.baiduMap;

import net.sf.json.JSONObject;


/**
 * 百度工具类
 * 
 * @author xuyw
 * @email xyw10000@163.com
 * @date 2014-06-22
 */
public class BaiDuUtil {
  public static String getCity(String lat, String lng) {
    JSONObject obj = getLocationInfo(lat, lng).getJSONObject("result").getJSONObject("addressComponent");
       
    return obj.getString("city");
  }

  public static JSONObject getLocationInfo(String lat, String lng) {
    String url = "http://api.map.baidu.com/geocoder/v2/?location=" + lat + ","
        + lng + "&output=json&ak=" +"ndpzwNS0VvRHGGbXAEO0KUcV"+"&pois=0";
    JSONObject obj = JSONObject.fromObject(HttpUtil.getRequest(url));
    return obj;
  }

  public static void main(String[] args) {
    System.out.println(BaiDuUtil.getCity("28.694439", "115.939728"));
  }
}