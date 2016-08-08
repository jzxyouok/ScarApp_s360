package com.zero2ipo.weixin.test;

import com.zero2ipo.weixin.config.Config;
import com.zero2ipo.weixin.utils.GetAccessTokenUtil;
import com.zero2ipo.weixin.utils.MenuUtil;
import net.sf.json.JSONObject;
public class Test {
    // 菜单创建（POST） 限100（次/天）
    public static String MENU_CREATE = MenuUtil.URL_MENU_CREATE;
    public static String token = GetAccessTokenUtil.getAccess_token2(Config.APPID, Config.SECRET);
    public String CreateMenu(String jsonMenu) {

        String resultStr = "";
        // 调用接口获取token
        System.out.println("Token========="+token);
        if (token != null) {
            // 调用接口创建菜单
            int result = createMenu(jsonMenu, token);
            // 判断菜单创建结果
            if (0 == result) {
                resultStr = "菜单创建成功";
            } else {
                resultStr = "菜单创建失败，错误码：" + result;
            }
            System.out.println(resultStr);
        }

        return resultStr;
    }


    /**
     * 创建菜单
     *
     * @param jsonMenu
     *            菜单的json格式
     * @param accessToken
     *            有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(String jsonMenu, String accessToken) {

        int result = 0;
        // 拼装创建菜单的url
        String url = MENU_CREATE+"?access_token="+accessToken;
        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
            }
        }

        return result;
    }
    public static int deleteMenu(String jsonMenu) {

        int result = 0;
        // 拼装创建菜单的url
        String url = MenuUtil.URL_MENU_DELETE+"?access_token="+token;
        // 调用接口创建菜单
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
            }
        }

        return result;
    }
    public static void main(String[] args) {
        // 这是一个符合菜单的json格式，“\”是转义符
        String jsonMenu = "{\n" +
                "    \"button\": [\n" +
                "        {\n" +
                "            \"name\": \"上门洗车\",\n" +
                "            \"sub_button\": [\n" +
                "                {\n" +
                "                    \"key\": \"\",\n" +
                "                    \"name\": \"上门洗车\",\n" +
                "                    \"type\": \"view\",\n" +
                "                    \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx889176fe2c1c0b0c&redirect_uri=http://wx.hongbaoxiche.cn/oauth/do.html&response_type=code&scope=snsapi_base&state=bsb1#wechat_redirect\"\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"我的\",\n" +
                "            \"sub_button\": [\n" +
                "                {\n" +
                "                    \"key\": \"\",\n" +
                "                    \"name\": \"洗车订单\",\n" +
                "                    \"type\": \"view\",\n" +
                "                    \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx889176fe2c1c0b0c&redirect_uri=http://wx.hongbaoxiche.cn/oauth/do.html&response_type=code&scope=snsapi_base&state=120#wechat_redirect\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"key\": \"\",\n" +
                "                    \"name\": \"洗车券\",\n" +
                "                    \"type\": \"view\",\n" +
                "                    \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx889176fe2c1c0b0c&redirect_uri=http://wx.hongbaoxiche.cn/oauth/do.html&response_type=code&scope=snsapi_base&state=130#wechat_redirect\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"key\": \"\",\n" +
                "                    \"name\": \"员工登陆\",\n" +
                "                    \"type\": \"view\",\n" +
                "                    \"url\": \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx889176fe2c1c0b0c&redirect_uri=http://wx.hongbaoxiche.cn/oauth/do.html&response_type=code&scope=snsapi_base&state=210#wechat_redirect\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
       System.out.println(jsonMenu);
        Test impl = new Test();
        impl.CreateMenu(jsonMenu);
    }
}
