package com.zero2ipo.mobile.action;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.OrderTongJiResult;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.common.token.AvoidDuplicateSubmission;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.core.md5.MD5PwdEncoder;
import com.zero2ipo.framework.util.DateUtil;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.mobile.web.SessionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 */
@Controller
public class UserAction {
	/**
	 * 个人中心
	 */
	@RequestMapping(value = "/user/option.html", method = RequestMethod.GET)
	public String myOptionPage(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		String rpage;
		//获取当前登录的用户id
        Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
        if(!StringUtil.isNullOrEmpty(user)){
        	user=userServices.findUserByMobile(user.getPhoneNum()).get(0);
        	model.put("user", user);
        	rpage=FmUtils.fmHtmlPage(request, model, MobilePageContants.FM_USER_OPTION);
        }else{
        	rpage=FmUtils.fmHtmlPage(request, model, MobilePageContants.FM_USER_LOGIN);
        }
        return rpage;
	}
	/**
	 * 用户基本信息注册
	 */
	@RequestMapping(value = "/user/register.html", method = RequestMethod.GET)
	public String registerPost(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return FmUtils.fmHtmlPage(request, model, MobilePageContants.BOOK_REGEDIT);
	}
	/**
	 * 用户基本信息注册
	 */
	@RequestMapping(value = "/user/register.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> registerPost(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			String mobile, String userPassword,String userIrId) {
		Map<String,Object> map=new HashMap<String, Object>();
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		Users user = userServices.saveUserCore(openId, mobile, userPassword,userIrId);
		if(user != null)
		{
			//如果用户存在，将用户添加进Session中
			SessionHelper.setAttribute(request, MobileContants.USER_SESSION_KEY, user);
			map.put("success", true);
			map.put("user", user);
		}else{
			map.put("success", false);
		}


		return map;
	}
	/**
	 * 修改手机号码页面
	 */
	@RequestMapping(value = "/user/updateMobile.html", method = RequestMethod.GET)
	public String updateMobile(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return FmUtils.fmHtmlPage(request, model, MobilePageContants.BOOK_UPDATE_MOBILE);
	}
	/**
	 * 用户基本信息注册
	 */
	@RequestMapping(value = "/user/updateMobile.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> updateMobilePost(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			String mobile) {
		Map<String,Object> map=new HashMap<String, Object>();
		boolean flg=false;
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
        if(!StringUtil.isNullOrEmpty(user)){
        	user.setPhoneNum(mobile);
        	flg=userServices.updateMobile(user);
        }
		map.put("success", flg);
		return map;
	}

	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @param model
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/resetPassword.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> resetPassword(HttpServletRequest request,
											   HttpServletResponse response, ModelMap model,
											   String mobile,String userPassword) {
		Map<String,Object> map=new HashMap<String, Object>();
		boolean flg=false;
		String message="";
		Users user=new Users();
		//if(!StringUtil.isNullOrEmpty(user)){
			user.setPhoneNum(mobile);
			user.setPassword(MD5PwdEncoder.generatePassword(userPassword));
			try {
				//判断此手机号码是否存在
				List<Users> users=userServices.findUserByMobile(mobile);
				if(null!=users&&users.size()>0){//手机号码存在，可以修改
					userServices.updateUserPassword(user);
					flg=true;
					message="修改成功";
				}else{
					flg=false;
					message="手机号码不存在";
				}
			}catch (Exception e){
				flg=false;
				message="修改失败";
				e.printStackTrace();
			}

		//}
		map.put("success", flg);
		map.put("message",message);
		return map;
	}
	/**
	 * 用户登录处理GET
	 */
	@RequestMapping(value = "/user/login.html", method = RequestMethod.GET)
	public String loginGet(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		return FmUtils.fmHtmlPage(request, model, MobilePageContants.FM_USER_LOGIN);
	}

	/**
	 * 用户登录处理POST 用户号码+密码登陆方式
	 */
	@RequestMapping(value = "/user/login.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> loginPost(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String mobile,
			String userPassword) {
		Map<String,Object> map=new HashMap<String, Object>();
		String page = "/";
		Users user = userServices.findUserByUsernameAndPassword(mobile, userPassword);
		if (user != null) {
			//更新用户opendi
			String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			if(!StringUtil.isNullOrEmpty(openid)){
				user.setOpenId(openid);
				userServices.updateUser(user);
			}

			String preUrl=SessionHelper.getStringAttribute(request, MobileContants.PAGE_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(preUrl)){
				page=preUrl;
			}
			//登陆成功后进去洗车券购买页面
			//page="/pay/buycouponPage.html";
			map.put("user",user);
			map.put("success",true);
			SessionHelper.setAttribute(request, MobileContants.USER_SESSION_KEY, user);
		} else {
			 //user = userServices.saveUserCore("", mobile, "");
			map.put("success",false);
		}
		map.put("page", page);
		return map;
	}
	/**
	 * 用户登陆操作：手机号码+验证码登录方式
	 */
	@RequestMapping(value = "/user/loginForCode.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> loginPostForMobileCode(HttpServletRequest request,
										HttpServletResponse response, ModelMap model, String mobile) {
		Map<String,Object> map=new HashMap<String, Object>();
		String page = "/index.html";
		Users user = userServices.findUserByUsernameAndPassword(mobile,"");
		String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if (user != null) {
			//更新用户opendi
			if(!StringUtil.isNullOrEmpty(openid)){
				user.setOpenId(openid);
				userServices.updateUser(user);
			}
			String preUrl=SessionHelper.getStringAttribute(request, MobileContants.PAGE_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(preUrl)){
				page=preUrl;
			}
			//登陆成功后进去洗车券购买页面
			//page="/pay/buycouponPage.html";
			map.put("user",user);
			map.put("success",true);
			SessionHelper.setAttribute(request, MobileContants.USER_SESSION_KEY, user);
		} else {
			//去数据库里面查询
			List<Users> userses=userServices.findUserByMobile(mobile);
			int total=0;
			if(!StringUtil.isNullOrEmpty(userses)&&userses.size()>0){
				total=userses.size();
			}
			if(total>0){
				user=userses.get(0);
				user.setOpenId(openid);
				userServices.updateUser(user);
			}else {
				user = userServices.saveUserCore(openid,mobile,"","");
			}
			//登陆成功，保存session
			map.put("user",user);
			SessionHelper.setAttribute(request, MobileContants.USER_SESSION_KEY, user);
			//登陆成功，保存user到全局session中
			WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
			wac.getServletContext().setAttribute(MobileContants.USER_APPLICATION_SESSION_KEY,user);
			map.put("success",true);
		}
		map.put("page", page);
		return map;
	}
	/**
	 * 洗车工登陆之后主页面
	 */
	@RequestMapping(value = "/adminIdex.html", method = RequestMethod.GET)
	public String adminIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,String userId,String status,String startDate,String endDate) {
		Map<String,Object> queryMap=new HashMap<String, Object>();
		if(!StringUtil.isNullOrEmpty(status)){
			 model.put("status", status);
			queryMap.put("status",status);
		}else{
			model.put("status", "1");
			queryMap.put("status",status);
		}
		if(!StringUtil.isNullOrEmpty(userId)){
			model.put("userId",userId);
			queryMap.put("userId",userId);
		}else{
			Object o=SessionHelper.getAttribute(request, MobileContants.ADMIN_SESSION_KEY);
			if(o instanceof AdminBo){
				AdminBo bo=(AdminBo)o;
				model.put("userId",bo.getUserId());
				queryMap.put("userId",bo.getUserId());
			}
		}
		if(MobileContants.status_4.equals(status)){
			if(!StringUtil.isNullOrEmpty(startDate)){
				model.put("startDate",startDate);
				queryMap.put("startDate",startDate+" 00:00:00");
			}else{
				model.put("startDate",DateUtil.getThisMonthStart());
				queryMap.put("startDate",DateUtil.getThisMonthStart()+" 00:00:00");
			}
			if(!StringUtil.isNullOrEmpty(endDate)){
				model.put("endDate",endDate);
				queryMap.put("endDate",endDate+" 23:59:59");
			}else {
				model.put("endDate",DateUtil.getThisMonthEnd());
				queryMap.put("endDate",DateUtil.getThisMonthEnd()+" 23:59:59");
			}
			OrderTongJiResult result=orderService.findOrderTongJiResult(queryMap);
			model.put("result",result);
		}
		//查询
		return FmUtils.fmHtmlPage(request, model, MobilePageContants.ADMIN_INDEX_PAGE);
	}
	/**
	 * 用户登录处理POST
	 */
	@RequestMapping(value = "/adminLogin.html", method = RequestMethod.POST)
	@AvoidDuplicateSubmission(needRemoveToken = true)
	@ResponseBody
	public Map<String,Object> adminLogin(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String mobile,
			String userPwd) {
		Map<String,Object> map=new HashMap<String, Object>();
		String page = "/adminIdex.html";
		Map<String,Object> queryMap=new HashMap<String, Object>();
		if(!StringUtil.isNullOrEmpty(mobile)){
			queryMap.put("mobile", mobile);
		}
		if(!StringUtil.isNullOrEmpty(userPwd)){
			queryMap.put("userPwd", MD5PwdEncoder.generatePassword(userPwd));
		}
		AdminBo admin = userServices.findAdminLoginMessage(queryMap);

		if (admin != null) {
			//洗车工首次登录才登陆成功之后，自动绑定微信
			String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			if(!StringUtil.isNullOrEmpty(openid)){
					admin.setIp(openid);
					userServices.updateAdmin(admin);
			}

			String preUrl=SessionHelper.getStringAttribute(request, MobileContants.PAGE_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(preUrl)){
				page=preUrl;
			}
			map.put("admin",admin);
			map.put("success",true);
			SessionHelper.setAttribute(request, MobileContants.ADMIN_SESSION_KEY, admin);
		} else {
			 //user = userServices.saveUserCore("", mobile, "");
			map.put("success",false);
		}
		map.put("page", page);
		return map;
	}
	/**
	 * 用户登录处理POST
	 */
	@RequestMapping(value = "/user/getUser.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> loginPost(HttpServletRequest request,
			HttpServletResponse response, ModelMap model
		) {
		Map<String,Object> map=new HashMap<String, Object>();
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			map.put("user", user);
		}else{
			map.put("user", null);
		}
		return map;
	}
    @Resource(name = "userServices")
    private IUserServices userServices ;
	@Resource(name = "orderService")
    private IOrderService orderService ;
}

