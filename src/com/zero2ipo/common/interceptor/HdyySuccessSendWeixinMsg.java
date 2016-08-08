package com.zero2ipo.common.interceptor;

import com.zero2ipo.core.MobileContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.module.entity.user.UserEntity;
import com.zero2ipo.weixin.config.Config;
import com.zero2ipo.weixin.contants.TemplateMessageContants;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.templateMessage.TemplateData;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 活动预约成功拦截器（预约成功之前发送一条微信消息    暂时没用）
 * @author zhengyunfei
 *
 */
public class HdyySuccessSendWeixinMsg extends HandlerInterceptorAdapter{
	@Resource(name = "coreService")
	private ICoreService coreService;
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		Object o = session.getAttribute(MobileContants.USER_SESSION_KEY);
		if(o != null && o instanceof UserEntity)
		{
			UserEntity user = (UserEntity)o;
			String openId=user.getOpenId();
			if(!StringUtil.isNullOrEmpty(openId)){
				//如果用户绑定了微信账号，则推送一条微信消息给用户
				WxTemplate wxTemplate=getWxTemplate(openId, TemplateMessageContants.TZYY_SUCCESS_TEMPLATE_MSGID,user);
				coreService.send_template_message(Config.APPID,Config.SECRET,openId,wxTemplate );
			}
		}
	
		return true;
	}

	/**
	 * 获取模板数据
	 * @return
	 */
	public WxTemplate getWxTemplate(String openId,String msgTemplateId,UserEntity user){
		WxTemplate temp = new WxTemplate();
		temp.setTouser(openId);
		temp.setTemplate_id(msgTemplateId);
		temp.setUrl("http://www.pestreet.cn/mobile/url/lzh/lzh.html");
		temp.setTopcolor("blue");
		/**根据会员id查询该会员预约的活动**/
		String userId=user.getUserId();
		if(!StringUtil.isNullOrEmpty(userId)){
			//InvitationLetterEntity hdyyEntity=invitationLetterService.findRecentlyHdyyByUserId(userId);
			Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
			TemplateData data0=new TemplateData();
			data0.setValue("尊敬的财富街会员,您已经成功预约本次会员活动,敬请留意详细信息");
			data0.setColor("blue");
			paramMap.put("first",data0);
			TemplateData data1=new TemplateData();
			data1.setValue("");
			data1.setColor("blue");
			paramMap.put("activity_name",data1);
			TemplateData data2=new TemplateData();
			data2.setValue("成功");
			data2.setColor("blue");
			paramMap.put("reserve_results",data2);
			TemplateData data3=new TemplateData();
			data3.setValue("");
			data3.setColor("blue");
			paramMap.put("activity_time",data3);
			temp.setData(paramMap);
			TemplateData data4=new TemplateData();
			data4.setValue("");
			data4.setColor("blue");
			paramMap.put("activity_address",data4);
			TemplateData remark=new TemplateData();
			remark.setValue("您还可以致电财富街客服专线，详情咨询<a href='tel:400-654-7828'>400-654-7828</a>");
			remark.setColor("blue");
			paramMap.put("remark",remark);
			temp.setData(paramMap);
		}

		return temp;
	}
}
