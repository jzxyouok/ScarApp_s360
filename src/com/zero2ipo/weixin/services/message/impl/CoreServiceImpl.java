package com.zero2ipo.weixin.services.message.impl;

import com.zero2ipo.weixin.dao.message.ICoreDao;
import com.zero2ipo.weixin.msg.Token;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import com.zero2ipo.weixin.test.WeixinUtil;
import com.zero2ipo.weixin.token.AccessToken;
import com.zero2ipo.weixin.token.TokenThread;
import com.zero2ipo.weixin.utils.CommonUtil;
import com.zero2ipo.weixin.utils.GetAccessTokenUtil;
import com.zero2ipo.weixin.utils.GetAllWeixinUserUtil;
import com.zero2ipo.weixin.utils.MessageUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 发送消息核心服务类
 */
@Component("coreService")
public class CoreServiceImpl implements ICoreService {
	/*
	 * 发送消息核心服务接口注入
	 */
	@Resource(name = "coreDao")
	private ICoreDao coreDao;
	/**
	 * 处理微信发来的请求
	 *
	 * @param request
	 * @return xml
	 */
	public  String processRequest(HttpServletRequest request,HttpServletResponse response) {
		return coreDao.processRequest(request,response);
	}
	/**
	 * 发送模板消息
	 * appId 公众账号的唯一标识
	 * appSecret 公众账号的密钥
	 * openId 用户标识
	 */
	public int send_template_message(String appId, String appSecret, String openId,WxTemplate template) {
		//定时获取token
		//AccessToken token=TokenThread.accessToken;
		//String access_token = token.getToken();
		//appSecret="812beb1774934d7d9ec9577f33f0856b";
		String access_token = GetAccessTokenUtil.getAccess_token2(appId, appSecret);
		System.out.println("获取到的acces_token================="+access_token);
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
		String jsonString = JSONObject.fromObject(template).toString();
		JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", jsonString);
		int result = 0;
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
			}
		}
		System.out.println("发送模板消息返回结果======="+result);
		return result;
	}
	/**
	 * 发送普通消息
	 * appId 公众账号的唯一标识
	 * appSecret 公众账号的密钥
	 * openId 用户标识
	 * msgTemplateId 模板ID
	 */
	public void send_message(String appId, String appSecret, String openId,String msgContent){
		//Token token = CommonUtil.getToken(appId, appSecret);
		AccessToken token=TokenThread.accessToken;
		String access_token = token.getToken();
		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
		JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", msgContent);
		int result = 0;
		if (null != jsonObject) {
			if (0 != jsonObject.getInt("errcode")) {
				result = jsonObject.getInt("errcode");
			}
		}
	}
	/**
	 * 获取微信关注者openid
	 * @return
	 */
	public String getOpenId(HttpServletRequest request, HttpServletResponse response){
		String openId="";
		Map<String, String> requestMap = null;
		try {
			requestMap = MessageUtil.parseXml(request);
			openId = requestMap.get("ToUserName");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openId;
	}
	public String getValue(String key){
		return coreDao.getValue(key);
	}
}
