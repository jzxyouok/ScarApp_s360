package com.zero2ipo.weixin.test;

import com.zero2ipo.weixin.config.Config;
import com.zero2ipo.weixin.utils.GetAccessTokenUtil;
import com.zero2ipo.weixin.utils.MenuUtil;
import net.sf.json.JSONObject;

public class CopyOfTest2 {
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
        String jsonMenu = "{\"button\":[" +
        "{\"name\":\"泡泡介绍\",\"sub_button\":[" +
        "{\"name\":\"服务介绍\",\"type\":\"view\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzIwODA2NzAxNg==&mid=400077223&idx=1&sn=0c146bd82dfceb35444ef4f3469cb2df#rd\"}," +
        "{\"name\":\"洗车价格\",\"type\":\"view\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzIwODA2NzAxNg==&mid=400564712&idx=1&sn=887bfca19c6f9786d2be0b438c97269d#rd\"}," +
        "{\"name\":\"招工信息\",\"type\":\"click\",\"key\":\"2\"}" +
         "]},"+ 

                                        "{\"name\":\"上门洗车\",\"type\":\"view\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb868e41533143338&redirect_uri=http://www.paopao7788.com/oauth/do.html&response_type=code&scope=snsapi_base&state=bsb1#wechat_redirect\"}" +
                                        "," +
                                        "{\"name\":\"我的\",\"sub_button\":[" +
                                        "{\"name\":\"最新活动\",\"type\":\"view\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MzIwODA2NzAxNg==&mid=400399688&idx=1&sn=69b4237254ecf95c0c01f043df37e340#rd\"}," +
                                        "{\"name\":\"洗车订单\",\"type\":\"view\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb868e41533143338&redirect_uri=http://www.paopao7788.com/oauth/do.html&response_type=code&scope=snsapi_base&state=120#wechat_redirect\"}," +
                                        "{\"name\":\"洗车券\",\"type\":\"view\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb868e41533143338&redirect_uri=http://www.paopao7788.com/oauth/do.html&response_type=code&scope=snsapi_base&state=130#wechat_redirect\"}," +
                                        "{\"name\":\"洗车工登陆\",\"type\":\"view\",\"url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxb868e41533143338&redirect_uri=http://www.paopao7788.com/oauth/do.html&response_type=code&scope=snsapi_base&state=210#wechat_redirect\"}" +
                                         "]}" +
        "]}";
       // String s = "{\"button\":[{\"name\":\"我的账户\",\"sub_button\":[{\"type\":\"click\",\"name\":\"账户绑定\",\"key\":\"M1001\"},{\"type\":\"click\",\"name\":\"我的资产\",\"key\":\"M1002\"}]},{\"type\":\"click\",\"name\":\"我的资产\",\"key\":\"M2001\"},{\"type\":\"click\",\"name\":\"其它\",\"key\":\"M3001\"}]}";
       System.out.println(jsonMenu);
        CopyOfTest2 impl = new CopyOfTest2();
       // impl.deleteMenu(jsonMenu);
        impl.CreateMenu(jsonMenu);
    }
}
