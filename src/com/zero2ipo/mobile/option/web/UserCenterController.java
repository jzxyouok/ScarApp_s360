package com.zero2ipo.mobile.option.web;

import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.core.MobileUserContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.module.entity.user.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Handles requests for the application home page.
 */
@Controller
public class UserCenterController {
	
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService ;
	private final static String defaultOpenId=MobileUserContants.DEFAULT_OPEN_ID;
	/**
	 * 个人中心
	 */
	@RequestMapping(value = "/user/center.html", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/user");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		int count=0;
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(openId)){
				queryMap.put("openId",openId);
				count=historyCarService.findAllListCount(queryMap);
			}else{
				openId=defaultOpenId;
				queryMap.put("openId",openId);
				count=historyCarService.findAllListCount(queryMap);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("hcount", count);
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 我的爱车
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/car.html", method = RequestMethod.GET)
	public ModelAndView centercar(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/lovecar");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 我的订单
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/order.html", method = RequestMethod.GET)
	public ModelAndView centerorder(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/orderlist");
		 Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
	     if(!StringUtil.isNullOrEmpty(user)){
	        	mv.addObject("mobile",user.getPhoneNum());
	      }else{
	    	  mv.setViewName(MobilePageContants.FM_USER_LOGIN);
	      }
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 常用地址
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/address.html", method = RequestMethod.GET)
	public ModelAndView centeraddress(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/loveaddress");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 购买优惠券
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/buycouponindex.html", method = RequestMethod.GET)
	public ModelAndView buycouponindex(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/payyouhuiquan");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 优惠券
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/coupon.html", method = RequestMethod.GET)
	public ModelAndView coupon(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/coupon");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
	/**
	 * 抢优惠券
	 * @param request
	 * @param response
	 * @param locale
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/center/checkcoupon.html", method = RequestMethod.GET)
	public ModelAndView checkcoupon(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		ModelAndView mv=new ModelAndView("mobile/user/checkcoupon");
		String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openId)){
			openId=defaultOpenId;
		}
		mv.addObject("openid", openId);
		return mv;
	}
}
