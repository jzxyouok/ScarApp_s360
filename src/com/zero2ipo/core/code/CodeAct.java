package com.zero2ipo.core.code;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.MESSAGEXsend;
import com.zero2ipo.SDK.utils.ConfigLoader;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.weixin.services.message.ICoreService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证码请求处理类
 *
 * @author zhengyunfei
 *
 */

@Controller
public class CodeAct {
	/**
	 * 图片验证码
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping(value = "/code/imgCode.act")
	public void randomValidateCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		response.setContentType("image/jpeg");// 设置相应类型,告诉浏览器输出的内容为图片
		response.setHeader("Pragma", "No-cache");// 设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		try {
			//randomValidateCode.getRandcode(request, response);// 输出图片方法
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手机验证码
	 * @param request
	 * @param response
	 * @param model
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value = "/code/mobileCode.act")
	@ResponseBody
	public Map<String,Object> mobileCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String telephone) {
		//获取验证码前，先检测一下手机号码是否已经存在
		Map<String,Object> map=new HashMap<String,Object>();
		//注册前查询手机号码是否已经存在
		List<Users> list=userServices.findUserByMobile(telephone);
		if(null!=list&&list.size()>0){
			map.put("success", "");
		}else{
			SendMessageVCode(request, telephone);
			map.put("success",true);
		}
		return map;
	}

	/**
	 * 发送短信验证码公用方法
	 * @param request
	 * @param telephone
	 */
	public void SendMessageVCode(HttpServletRequest request, String telephone) {
		VerificationCode verificationCode=new VerificationCode();
		//获取随机验证码
		String vCode = verificationCode.generate(request);
		System.out.println("cc====="+vCode);
		AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
		String messageAppId=coreService.getValue(CodeCommon.MESSAGE_APPID);
		String messageAppKey=coreService.getValue(CodeCommon.MESSAGE_APPKEY);
		config.setAppId(messageAppId);
		config.setAppKey(messageAppKey);
		MESSAGEXsend submail = new MESSAGEXsend(config);
		submail.addTo(telephone);
		String projectId=coreService.getValue(CodeCommon.MESSAGEKEY);
		submail.setProject(projectId);
		submail.addVar("code", vCode);
		submail.xsend();
	}


	/**
	 * 手机验证码
	 * @param request
	 * @param response
	 * @param model
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value = "/code/loginMobileCode.act")
	@ResponseBody
	public Map<String,Object> loginMobileCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String telephone) {
			//获取验证码前，先检测一下手机号码是否已经存在
			Map<String,Object> map=new HashMap<String,Object>();
			SendMessageVCode(request, telephone);
			map.put("success",true);
			return map;
	}
	/**
	 * 验证手机验证码
	 * @param request
	 * @param response
	 * @param model
	 * @param telephone
	 * @return
	 */
	@RequestMapping(value = "/code/getMobileCode.act")
	@ResponseBody
	public Map<String,Object> getMobileCode(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String vcode,String mobile) {
		Map<String,Object> map=new HashMap<String,Object>();
		String code=(String) request.getSession().getAttribute(MobileContants.VERIFICATION_CODE);
		if(vcode.equals(code)||mobile.equals(MobileContants.ADMIN_MOBILE)){//管理员手机号免验证码登录
			map.put("success", true);
		}else{
			map.put("success", false);
		}
		return map;
	}
	/**
	 * 反回结果结构
	 */
	public String sendResult(String result) {
		JSONObject json = new JSONObject();
		try {
			//判断是否有反回结果
			if(result != null && !"".endsWith(result))
			{
				//返回结果不为负数表示发送成功
				if(!result.startsWith("-"))
				{
					json.put("result", "true");
					json.put("error", "");
				}
				else
				{
					json.put("result", "false");
					//json.put("error", errorMessage);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	/*
	 * 图片验证码生成器注入
	 */
	//@Resource(name = "randomValidateCode")
	//private RandomValidateCode randomValidateCode;

	/*
	 * 手机验证码生成器注入
	 */
	//@Resource(name = "verificationCode")
	//private VerificationCode verificationCode;

	/*
	 * 短信记录服务接口注入
	 */
	//@Resource(name = "telMessageService")
	//private ITelMessageService telMessageService;

	/*
	 * 发送处理类注入
	 */
//@Resource(name="sendMessage")
	//private SendMessage sendMessage;
	@Resource(name="userServices")
	private IUserServices userServices;
	@Resource(name="coreService")
	private ICoreService coreService;
}
