package com.zero2ipo.weixin.action;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.gkk.IGkkService;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.plugins.contants.PluginsContants;
import com.zero2ipo.weixin.contants.UrlContants;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.utils.HttpUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/10.
 */
@Controller
public class Oauth2Servlet {

    private static final long serialVersionUID = -644518508267758016L;
    @RequestMapping(value = "/oauth/do.html", method = RequestMethod.GET)
    public ModelAndView oauth(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        FmUtils.FmData(request, model);
         String get_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=APPID" +
                "&secret=SECRET&" +
                "code=CODE&grant_type=authorization_code";
         String get_userinfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

        String page=MobilePageContants.FM_USER_LOGIN;
        ModelAndView mv = new ModelAndView();
        try {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            // xml请求解析
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String code = request.getParameter("code");
            //判断页面跳转
            String key=request.getParameter("state");
            String appid=coreService.getValue(CodeCommon.APPID);
            String appsecret=coreService.getValue(CodeCommon.APPSECRET);
            get_access_token_url = get_access_token_url.replace("APPID", appid);
            get_access_token_url = get_access_token_url.replace("SECRET", appsecret);
            get_access_token_url = get_access_token_url.replace("CODE", code);

            String json = HttpUtil.getUrl(get_access_token_url);
            String openid="";
            //获取当前登录的用户id

            JSONObject jsonObject = JSONObject.fromObject(json);
            if(jsonObject.has("openid")){
            	 openid = jsonObject.getString("openid");
            }
            System.out.println("第一个openid==="+openid);
            Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
            if(!StringUtil.isNullOrEmpty(openid)){
            	//根据openid查询user
            	Map<String,Object> queryMap=new HashMap<String,Object>();
            	queryMap.put("openId", openid);
            	Users u=userServices.findUserByMap(queryMap);
                System.out.println("从数据库中查询出来的openid=================="+u);
            	if(!StringUtil.isNullOrEmpty(u)&&null!=u){
            		SessionHelper.setAttribute(request, MobileContants.USER_SESSION_KEY, u);
                    user=u;
            	}else{
                    if(!StringUtil.isNullOrEmpty(user)){
                        user.setOpenId(openid);
                        userServices.updateUser(user);//更新用户openId
                    }

                }
            	SessionHelper.setAttribute(request, MobileContants.USER_OPEN_ID_KEY, openid);
            }


            if(StringUtil.isNullOrEmpty(user)){
            	page=MobilePageContants.FM_USER_LOGIN;
            }else{
            	   if(!StringUtil.isNullOrEmpty(key)){//在线下单
                       if(key.equals(UrlContants.MENU_KEY_11)){
                           //page="mobile/xc/main";
                           page=MobilePageContants.FM_PAGE_MAIN;
                         /* ServletContext application =request.getSession().getServletContext();
                           String access_token = GetAccessTokenUtil.getAccess_token2(appid,appsecret);
                           CarAction.sweepParam(request,mv,appid,access_token);*/
                           model=gkkService.initGkkPage(model,openid,user);
                       }
                       if(key.equals(UrlContants.MENU_KEY_INDEX)){
                           page=MobilePageContants.FM_PAGE_MAIN;
                           model=gkkService.initGkkPage(model,openid,user);
                       }
                       if(key.equals(UrlContants.MENU_KEY_12)){//洗车订单
                           page=MobilePageContants.FM_LZH;
                           model=gkkService.initMyOrder(model,openid,page,user);
                       }
                       if(key.equals(UrlContants.MENU_KEY_13)){//洗车券
                           page=MobilePageContants.FM_PAGE_WDXCQ;
                           model=gkkService.initMyOrder(model,openid,page,user);
                       }
                       System.out.println("key==============="+key);
                       if(key.equals(UrlContants.MENU_KEY_31)){//钱包
                           page=MobilePageContants.MY_MONEY_PAGE;
                           //model=gkkService.initMyOrder(model,openid,page,user);
                       }

                   }
            }
            if(key.equals(UrlContants.MENU_KEY_32)){//大转盘
                page= PluginsContants.DA_ZHUAN_PAN_PAGE;
                //model=gkkService.initMyOrder(model,openid,page,user);
            }
            if(key.equals(UrlContants.MENU_KEY_21)){//洗车工登陆
                //page=MobilePageContants.ADMIN_LOGIN_PAGE;
                if(!StringUtil.isNullOrEmpty(openid)){
                    //根据openid查询user
                    Map<String,Object> queryMap=new HashMap<String,Object>();
                    queryMap.put("openId", openid);
                    AdminBo bo=userServices.findAdminLoginMessage(queryMap);
                    if(!StringUtil.isNullOrEmpty(bo)&&null!=bo){
                        SessionHelper.setAttribute(request, MobileContants.ADMIN_SESSION_KEY, bo);
                        model.put("status", "0");
                        page =MobilePageContants.ADMIN_INDEX_PAGE;
                    }else{
                        page=MobilePageContants.ADMIN_LOGIN_PAGE;
                    }
                    SessionHelper.setAttribute(request, MobileContants.USER_OPEN_ID_KEY, openid);
                }
            }
            if(key.equals(UrlContants.MENU_KEY_22)){//切换账号登陆
                page=MobilePageContants.FM_USER_LOGIN;
            }
            mv.setViewName(page);

            mv.addObject("openId",openid);
            System.out.println("第一个用户oid========="+openid);
           // String access_token = jsonObject.getString("access_token");
           // get_userinfo = get_userinfo.replace("ACCESS_TOKEN", access_token);
          //  get_userinfo = get_userinfo.replace("OPENID", openid);
//
           // String userInfoJson = HttpUtil.getUrl(get_userinfo);

           // JSONObject userInfoJO = JSONObject.fromObject(userInfoJson);

          //  String user_openid = userInfoJO.getString("openid");

           // String user_nickname = userInfoJO.getString("nickname");
           // String user_sex = userInfoJO.getString("sex");
            //String user_province = userInfoJO.getString("province");
           // String user_city = userInfoJO.getString("city");
            //String user_country = userInfoJO.getString("country");
           // String user_headimgurl = userInfoJO.getString("headimgurl");
            response.setContentType("text/html; charset=utf-8");
            SessionHelper.setAttribute(request, MobileContants.USER_OPEN_ID_KEY, openid);
        } catch (Exception e) {
             e.printStackTrace();
        }
        return mv;
    }

    /*
 * 模版服务接口注入
 */
    @Resource(name = "gkkService")
    private IGkkService gkkService;
    @Resource(name = "userServices")
    private IUserServices userServices;
    @Resource(name="coreService")
    private ICoreService coreService;

}
