package com.zero2ipo.weixin.contants;

/**
 * Created by Administrator on 2015/7/22.
 */
public class UrlContants {
    public final static String get_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?" +
            "appid=APPID" +
            "&secret=SECRET&" +
            "code=CODE&grant_type=authorization_code";
    public final static String get_userinfo_url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**微信公众号自定义菜单key**/
    public final static  String MENU_KEY_11="bsb1";//
    public final static  String MENU_KEY_INDEX="index";//首页
    public final static  String MENU_KEY_12="120";//
    public final static  String MENU_KEY_13="130";//
    public final static  String MENU_KEY_21="210";//
    public final static  String MENU_KEY_22="220";//
    public final static  String MENU_KEY_31="310";//
    public final static  String MENU_KEY_32="320";//
}
