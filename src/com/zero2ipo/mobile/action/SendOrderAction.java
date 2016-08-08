package com.zero2ipo.mobile.action;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.MESSAGEXsend;
import com.zero2ipo.SDK.utils.ConfigLoader;
import com.zero2ipo.common.domain.Upload;
import com.zero2ipo.common.entity.*;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.framework.util.DateUtil;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.bsb.ISendOrderService;
import com.zero2ipo.mobile.services.config.IConfManage;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.templateMessage.TemplateData;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 */
@Controller
public class SendOrderAction {

	@Resource(name = "sendOrderService")
	private ISendOrderService sendOrderService ;
	@Autowired
	private IOrderService orderService;
	@Resource(name = "confManage")
	private IConfManage confManage;
	@Autowired
	private IUserServices userServices;
	/*
     * 核心服务接口注入
     */
	@Autowired
	public ICoreService coreService;

	/*
	 * 跨域上传
	 */
	@Resource(name = "domainUpload")
	public Upload upload;

	/**
	 * update washcar before after photo
	 */
	@RequestMapping(value = "/updateSendOrder.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> completeWashCar(HttpServletRequest request,
									HttpServletResponse response, ModelMap model, SendOrder sendOrder,RedirectAttributes redirectAttributes ) {
		String orderId=sendOrder.getOrderId();
		Map<String, Object> resultMap=new HashMap<String, Object>();
		boolean flag=completeWashCar(request,response,model,sendOrder);
		if(flag){
			resultMap.put("orderId", orderId);
			resultMap.put("success",true);
		}
		//String url="renwu/order"+orderId+"/f4"+".html";
		//return "redirect:/"+url;
		return resultMap;
	}
	/**
	 *开始洗车，修改洗车状态为开始洗车
	 */
	@RequestMapping(value = "/updateSendOrderStatus.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object>  updateSendOrderStatus(HttpServletRequest request,
										HttpServletResponse response, ModelMap model, SendOrder sendOrder,RedirectAttributes redirectAttributes ) {
		String orderId=sendOrder.getOrderId();
		Map<String, Object> resultMap=new HashMap<String, Object>();
		boolean flag=startWashCar(request, response, model, sendOrder);
		if(flag){
			resultMap.put("orderId", orderId);
			resultMap.put("success",true);
			//根据订单id查询订单信息
			Map<String,Object> orderMap=new HashMap<String, Object>();
			orderMap.put("id",orderId);
			Order order=orderService.findById(orderMap);
			String domain=getValue(CodeCommon.DOMAIN);
			String url=domain+"/f/order"+orderId+".html";
			//发送短信通知
			String isSendMessage=coreService.getValue(CodeCommon.IS_SENDMESSAGE_TO_ADMIN);
			if(CodeCommon.IS_SENDMESSAGE_TO_ADMIN_FLAG.equals(isSendMessage)){//开启给管理发送派单短信通知
				String sendMessageFlag=coreService.getValue(CodeCommon.SEND_MESSAGE_FLAG);
				String startWashCarMessageKey=coreService.getValue(CodeCommon.START_WASHCAR_DUANXIN_MESSAGE);
				if(sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_DUANXIN)){
					//发送短信通知
					if(!StringUtil.isNullOrEmpty(order)){
						String content=url;
						SendMessageVCode(request,order.getMobile(),content,startWashCarMessageKey);
					}
				}
				if(sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_WEIXIN)) {
					String keyword1 = "";
					String keyword2 = "";
					String keyword3 = "";
					String keyword4 = "";
					if (!StringUtil.isNullOrEmpty(order)) {
						keyword1 = order.getOutTradeNo();
						keyword2 = order.getWashTime();
						keyword3 = order.getWashType();
					}
					//根据订单id查询洗车工信息姓名和手机号码
					Map<String, Object> queryMap = new HashMap<String, Object>();
					queryMap.put("orderId", orderId);
					AdminBo adminBo = userServices.findAdminLoginMessage(queryMap);
					String name = "";
					String mobile = "";
					if (!StringUtil.isNullOrEmpty(adminBo)) {
						name = adminBo.getUserName();
						mobile = adminBo.getMobile();
						keyword4 = name + " " + mobile;
						//发送洗车开始通知
						ConfValue confValue;
						//是否test模板消息
						String openId = getValue(CodeCommon.TEST_TEMPLATE_MESSAGE);
						if (StringUtil.isNullOrEmpty(openId) || "null".equals(openId)) {
							Users userEntity = userServices.findUserByMap(orderMap);
							if (!StringUtil.isNullOrEmpty(userEntity)) {
								openId = userEntity.getOpenId();
							}
						}

						String templateMessageId = getValue(CodeCommon.WASH_CAR_START_MESSAGE);
						WxTemplate wxTemplate = getStartWxTemplate(openId, templateMessageId, url, keyword1, keyword2, keyword3, keyword4);
						String appid = getValue(CodeCommon.APPID);
						String appscret = getValue(CodeCommon.APPSECRET);
						coreService.send_template_message(appid, appscret, openId, wxTemplate);
					}

				}

			}
		}
		//String url="renwu/order"+orderId+"/f3"+".html";
		//return "redirect:/"+url;
		return resultMap;
	}
/**
 * 发送短信方法更改
 * @param request
 * @param telephone
 */
	public void SendMessageVCode(HttpServletRequest request, String telephone,String content,String projectId) {
		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		String messageAppId=coreService.getValue(CodeCommon.MESSAGE_APPID);
		String messageAppKey=coreService.getValue(CodeCommon.MESSAGE_APPKEY);
		config.setAppId(messageAppId);
		config.setAppKey(messageAppKey);
		MESSAGEXsend submail = new MESSAGEXsend(config);
		submail.addTo(telephone);
		//String projectId=coreService.getValue(CodeCommon.MESSAGEKEY);
		submail.setProject(projectId);
		submail.addVar("var1", content);
		submail.xsend();
	}
	/**
	 * 根据key获取value
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		String result="";
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("confKey",key);
		ConfValue  confValue=confManage.findConfValueByMap(m);
		result=confValue.getConfValue();
		return result;
	}

	/**
	 * 洗车完成
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public boolean completeWashCar(HttpServletRequest request,
								 HttpServletResponse response, ModelMap model,SendOrder sendOrder) {

		String orderId=sendOrder.getOrderId();
		try {
				if(!StringUtil.isNullOrEmpty(sendOrder)){
					sendOrder.setStatus("4");
				}
				boolean u = sendOrderService.updSendOrder(sendOrder);
				if(u)
				{
					//发送短信通知
					String isSendMessage=coreService.getValue(CodeCommon.IS_SENDMESSAGE_TO_ADMIN);
					if(CodeCommon.IS_SENDMESSAGE_TO_ADMIN_FLAG.equals(isSendMessage)) {//开启给管理发送派单短信通知
						String sendMessageFlag = coreService.getValue(CodeCommon.SEND_MESSAGE_FLAG);
						String completeWashCarMessageKey = coreService.getValue(CodeCommon.COMPLETE_WASHCAR_DUANXIN_MESSAGE);
						if (sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_DUANXIN)) {
							Map<String,Object> orderMap=new HashMap<String, Object>();
							orderMap.put("id",orderId);
							Order order=orderService.findById(orderMap);
							//发送短信通知
							if (!StringUtil.isNullOrEmpty(order)) {
								String domain=coreService.getValue(CodeCommon.DOMAIN);
								//String url=domain+"/renwu/order"+orderId+"/f4"+".html";
								String url=domain+"/f/order"+orderId+".html";
								SendMessageVCode(request, order.getMobile(), url, completeWashCarMessageKey);
							}
						}
						if (sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_WEIXIN)) {
							Map<String, Object> m=new HashMap<String, Object>();
							String templateMessageId="";
							String appid="";
							String appscret="";
							m.put("confKey", CodeCommon.WASH_CAR_COMPLETE_MESSAGE);
							ConfValue confValue=confManage.findConfValueByMap(m);
							templateMessageId=confValue.getConfValue();
							m.put("confKey", CodeCommon.APPID);
							confValue=confManage.findConfValueByMap(m);
							appid=confValue.getConfValue();
							m.put("confKey", CodeCommon.APPSECRET);
							confValue=confManage.findConfValueByMap(m);
							appscret=confValue.getConfValue();
							if(!StringUtil.isNullOrEmpty(confValue)){
								String openId="";
								Map<String, Object> queryMap=new HashMap<String, Object>();
								queryMap.put("id", orderId);
								Users userEntity=userServices.findUserByMap(queryMap);
								if(!StringUtil.isNullOrEmpty(userEntity)){
									openId=userEntity.getOpenId();
								}
								SendTemplateMessage(orderId, m, templateMessageId,
										appid, appscret, openId);

							}

						}
					}


					return u;
				}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * start wash car
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public boolean startWashCar(HttpServletRequest request,
								 HttpServletResponse response, ModelMap model, SendOrder sendOrder) {
		boolean flg=false;
		try {
				if(!StringUtil.isNullOrEmpty(sendOrder)){
					sendOrder.setStatus("3");
				}
				flg = sendOrderService.updSendOrder(sendOrder);
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return flg;

	}
	/**
	 * 洗车完成发送模板消息通知
	 * @param orderId
	 * @param m
	 * @param templateMessageId
	 * @param appid
	 * @param appscret
	 * @param openId
	 */
	private void SendTemplateMessage(String orderId, Map<String, Object> m,
									 String templateMessageId, String appid, String appscret,
									 String openId) {
		ConfValue confValue;
		//是否test模板消息
		m.put("confKey", CodeCommon.TEST_TEMPLATE_MESSAGE);
		ConfValue testConfValue=confManage.findConfValueByMap(m);
		//if(!StringUtil.isNullOrEmpty(testConfValue)&&!StringUtil.isNullOrEmpty(testConfValue.getConfValue())){
		//	openId=testConfValue.getConfValue();
	//	}

		m.put("confKey", CodeCommon.DOMAIN);
		confValue=confManage.findConfValueByMap(m);
		String domain=confValue.getConfValue();
		String url=domain+"/f/order"+orderId+".html";
		WxTemplate wxTemplate=getWxTemplate(openId,templateMessageId,url);

		coreService.send_template_message(appid,appscret,openId,wxTemplate);
	}

	/**
	 * 洗车完成模板
	 * @return
	 */
	public WxTemplate getWxTemplate(String openId,String msgTemplateId,String url){
		WxTemplate temp = new WxTemplate();
		temp.setTouser(openId);
		temp.setTemplate_id(msgTemplateId);
		temp.setUrl(url);
		temp.setTopcolor("#000000");

		Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
		TemplateData data0=new TemplateData();
		data0.setValue("尊敬的客戶您好，您的爱车已经为您洗好");
		data0.setColor("#040188");
		paramMap.put("first",data0);

		TemplateData data1=new TemplateData();
		data1.setValue("已完成");
		data1.setColor("#040188");
		paramMap.put("keyword1",data1);

		TemplateData rmTime=new TemplateData();
		rmTime.setValue(DateUtil.getCurrentDate());
		rmTime.setColor("#040188");
		paramMap.put("keyword2",rmTime);
		temp.setData(paramMap);
		TemplateData remark=new TemplateData();
		remark.setValue("感谢您选择我们的服务,请您点击连接对此次服务进行评价");
		remark.setColor("#040188");
		paramMap.put("remark",remark);
		temp.setData(paramMap);

		return temp;
	}
	/**
	 * 洗车开始模板数据获取模板数据
	 * @return
	 */
	public WxTemplate getStartWxTemplate(String openId,String msgTemplateId,String url,String keyword1,String keyword2,String keyword3,String keyword4){
		WxTemplate temp = new WxTemplate();
		temp.setTouser(openId);
		temp.setTemplate_id(msgTemplateId);
		temp.setUrl(url);
		temp.setTopcolor("#000000");
		Map<String,TemplateData> paramMap=new HashMap<String, TemplateData>();
		TemplateData data0=new TemplateData();
		data0.setValue("尊敬的客户您好，师傅已开始为您的爱车服务");
		data0.setColor("#040188");
		paramMap.put("first",data0);

		TemplateData data1=new TemplateData();
		data1.setValue("已开始");
		data1.setColor("#040188");
		paramMap.put("keyword1",data1);
		TemplateData data2=new TemplateData();
		data2.setValue(keyword2);
		data2.setColor("#040188");
		paramMap.put("keyword2",data2);

		TemplateData data3=new TemplateData();
		data3.setValue(keyword3);
		data3.setColor("#040188");
		paramMap.put("keyword3",data3);

		TemplateData data4=new TemplateData();
		data4.setValue(keyword4);
		data4.setColor("#040188");
		paramMap.put("keyword4",data4);
		TemplateData remark=new TemplateData();
		remark.setValue("如有问题，请联系我们的师傅。");
		remark.setColor("#040188");
		paramMap.put("remark",remark);
		temp.setData(paramMap);
		return temp;
	}
}

