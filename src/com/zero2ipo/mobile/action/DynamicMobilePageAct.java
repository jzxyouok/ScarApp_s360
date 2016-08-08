package com.zero2ipo.mobile.action;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.io.FileHelper;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.bsb.IWashCouponService;
import com.zero2ipo.mobile.utils.DateUtil;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.mobile.web.URLHelper;
import com.zero2ipo.pay.web.CarAction;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.utils.GetAccessTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 手机端主页调整控制
 * @author zhengyunfei
 * @date 2015-04-22
 *
 */

@Controller
public class DynamicMobilePageAct {

	/**
	 * 跟路径控制
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/washcar/index.html", method = RequestMethod.GET)
	public ModelAndView index(HttpServletRequest request,
							  HttpServletResponse response, ModelMap model,String couponId,String carType) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		try {
			mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
			//获取当前登录的用户id
			Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(user)){
				System.out.println("userId2====================================================="+user.getUserId());
				//if(user.getUserId().equals("13717625140")){
				//	mv.setViewName("mobile/xc/test");
					//首先从缓存中获取appid
					ServletContext application =request.getSession().getServletContext();
					String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
					String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
					String access_token = GetAccessTokenUtil.getAccess_token2(appId,appSecret);
					CarAction.sweepParam(request,mv,appId,access_token);
				//}
				mv.addObject("user",user);
				List<Car> list=new ArrayList<Car>();
				Car car=null;
				Map<String,Object> queryMap=new HashMap<String,Object>();
				String userId= user.getUserId();
				mv.addObject("userId",userId);
				queryMap.put("mobile", user.getPhoneNum());
				queryMap.put("userId", userId);
				list=historyCarService.findAllList(queryMap);
				String days=coreService.getValue(CodeCommon.PRE_TIME_DAYS);
				String hours=coreService.getValue(CodeCommon.PRE_TIME_HOURS);
				List<String> preDates=DateUtil.getLast2Hours(Integer.parseInt(days),Integer.parseInt(hours));
				mv.addObject("preDates", preDates);
				request.getSession().setAttribute(MobileContants.PRE_DATES_KEY,preDates);//保存录入的车辆信息到缓存中
				if(!StringUtil.isNullOrEmpty(couponId)){
					mv.addObject("couponId",couponId);
					//根据洗车券id查询洗车券信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", couponId);
					GgwashCoupon washCoupon=washCouponService.findById(map);
					if(!StringUtil.isNullOrEmpty(washCoupon)){
						mv.addObject("washCoupon", washCoupon);
						mv.addObject("couponName", washCoupon.getName());
					}

				}
				mv.addObject("carList",list);
				Car edite=(Car) SessionHelper.getAttribute(request, MobileContants.CAR_SESSION_KEY);
				if(list.size()>0){
					car=list.get(0);
					if(!StringUtil.isNullOrEmpty(edite)&&!StringUtil.isNullOrEmpty(edite.getCarNo())){//保存刚刚录入的car信息
						edite.setId(car.getId());
						car=edite;
					}
					//car.setCarNo(user.getAccount());

				}else{

					if(!StringUtil.isNullOrEmpty(edite)&&!StringUtil.isNullOrEmpty(edite.getCarNo())){//保存刚刚录入的car信息
						car=edite;
					}else{
						car =new Car();
					}
					//car.setCarNo(user.getAccount());
				}
				car.setMobile(user.getPhoneNum());
				//保存车型
				if(!StringUtil.isNullOrEmpty(carType)){
					car.setCarType(carType);
				}
				mv.addObject("bo",car);
				request.getSession().setAttribute(MobileContants.CAR_SESSION_KEY,car);//保存录入的车辆信息到缓存中
			}else{
				mv.setViewName(MobilePageContants.FM_USER_LOGIN);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 跟路径控制
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/washcar/indexPage.html", method = RequestMethod.GET)
	public ModelAndView menuIndexPage(HttpServletRequest request,
									  HttpServletResponse response, ModelMap model,String couponId,String carType) {
		FmUtils.FmData(request, model);
		ModelAndView mv = new ModelAndView();
		ServletContext application =request.getSession().getServletContext();
		String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
		String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
		String domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";
		String access_token = GetAccessTokenUtil.getAccess_token2(appId,appSecret);
		CarAction.sweepParam(request,mv,appId,access_token);
		String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state=bsb1#wechat_redirect";
		mv.setViewName("redirect:"+url);
		return mv;
	}
		/**
         * 跟路径控制
         * @param request
         * @param response
         * @param model
         * @return
         */
	@RequestMapping(value = "/washcar/indexOfLogin.html", method = RequestMethod.GET)
	public ModelAndView indexOfLogin(HttpServletRequest request,
							  HttpServletResponse response, ModelMap model,String couponId,String carType) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		try {
			mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
			//首先从缓存中获取appid
			//HttpRequest.sendGet(url,"");
			//获取当前登录的用户id
			Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(user)){
				//if(user.getUserId().equals("13717625140")){
				//	mv.setViewName("mobile/xc/test");

				//}
				mv.addObject("user",user);
				List<Car> list=new ArrayList<Car>();
				Car car=null;
				Map<String,Object> queryMap=new HashMap<String,Object>();
				String userId= user.getUserId();
				mv.addObject("userId",userId);
				queryMap.put("mobile", user.getPhoneNum());
				queryMap.put("userId", userId);
				list=historyCarService.findAllList(queryMap);
				String days=coreService.getValue(CodeCommon.PRE_TIME_DAYS);
				String hours=coreService.getValue(CodeCommon.PRE_TIME_HOURS);
				List<String> preDates=DateUtil.getLast2Hours(Integer.parseInt(days),Integer.parseInt(hours));
				mv.addObject("preDates", preDates);
				request.getSession().setAttribute(MobileContants.PRE_DATES_KEY,preDates);//保存录入的车辆信息到缓存中
				if(!StringUtil.isNullOrEmpty(couponId)){
					mv.addObject("couponId",couponId);
					//根据洗车券id查询洗车券信息
					Map<String,Object> map=new HashMap<String,Object>();
					map.put("id", couponId);
					GgwashCoupon washCoupon=washCouponService.findById(map);
					if(!StringUtil.isNullOrEmpty(washCoupon)){
						mv.addObject("washCoupon", washCoupon);
						mv.addObject("couponName", washCoupon.getName());
					}

				}
				mv.addObject("carList",list);
				Car edite=(Car) SessionHelper.getAttribute(request, MobileContants.CAR_SESSION_KEY);
				if(list.size()>0){
					car=list.get(0);
					if(!StringUtil.isNullOrEmpty(edite)&&!StringUtil.isNullOrEmpty(edite.getCarNo())){//保存刚刚录入的car信息
						edite.setId(car.getId());
						car=edite;
					}
					//car.setCarNo(user.getAccount());

				}else{

					if(!StringUtil.isNullOrEmpty(edite)&&!StringUtil.isNullOrEmpty(edite.getCarNo())){//保存刚刚录入的car信息
						car=edite;
					}else{
						car =new Car();
					}
					//car.setCarNo(user.getAccount());
				}
				car.setMobile(user.getPhoneNum());
				//保存车型
				if(!StringUtil.isNullOrEmpty(carType)){
					car.setCarType(carType);
				}
				mv.addObject("bo",car);
				request.getSession().setAttribute(MobileContants.CAR_SESSION_KEY,car);//保存录入的车辆信息到缓存中
				//发送get请求获取openId
				//HttpRequest.sendGet("https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state=index#wechat_redirect","");
			}else{
				ServletContext application =request.getSession().getServletContext();
				String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
				String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
				String domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";
				String access_token = GetAccessTokenUtil.getAccess_token2(appId,appSecret);
				CarAction.sweepParam(request,mv,appId,access_token);
				String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state=bsb1#wechat_redirect";
				mv.setViewName("redirect:"+url);
				//mv.setViewName(MobilePageContants.FM_USER_LOGIN);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
	/**
	 * 首页
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.html", method = RequestMethod.GET)
	public ModelAndView indexPage(HttpServletRequest request,
							  HttpServletResponse response, ModelMap model,String couponMoney,String vipCouponId) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		try {
			mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
			//获取当前登录的用户id
			Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(user)){
				Car car= (Car) request.getSession().getAttribute(MobileContants.CAR_SESSION_KEY);
				mv.addObject("bo",car);
				mv.addObject("couponMoney", couponMoney);
				List<String> preDates= (List<String>) request.getSession().getAttribute(MobileContants.PRE_DATES_KEY);//保存录入的车辆信息到缓存中
				mv.addObject("preDates",preDates);
				//if(user.getUserId().equals("13717625140")){
					//mv.setViewName("mobile/xc/test");
					//首先从缓存中获取appid
					ServletContext application =request.getSession().getServletContext();
					String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
					String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
					String access_token = GetAccessTokenUtil.getAccess_token2(appId,appSecret);
					CarAction.sweepParam(request,mv,appId,access_token);
				//}
			}else{
				mv.setViewName(MobilePageContants.FM_USER_LOGIN);
			}
			//当用户选择优惠券的时候，把此优惠券id放到缓存中,等微信支付成功的时候，要从缓存中取出优惠券id，然后把状态更改为已使用
			//if(!StringUtil.isNullOrEmpty(vipCouponId)){
				//request.getSession().setAttribute(MobileContants.VIP_COUPON_ID_KEY,vipCouponId);
			//}
			//首先从缓存中获取appid
			/*ServletContext application =request.getSession().getServletContext();
			String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
			String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
			String access_token = GetAccessTokenUtil.getAccess_token2(appId,appSecret);
			CarAction.sweepParam(request,mv,appId,access_token);*/
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mv;
	}

	/**
	 * 我的洗车券
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mycoupon.html", method = RequestMethod.GET)
	public ModelAndView wycj(HttpServletRequest request,
							 HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		//获取当前登录的用户id
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			mv.setViewName(MobilePageContants.FM_PAGE_WDXCQ);
			mv.addObject("user", user);
			mv.addObject("mobile",user.getPhoneNum());
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}

		return mv;
	}
	/**
	 * 我的洗车券
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/mycoupon.html", method = RequestMethod.POST)
	public ModelAndView wycjForPost(HttpServletRequest request,
									HttpServletResponse response, ModelMap model,Car car) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		//获取当前登录的用户id
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			mv.setViewName(MobilePageContants.FM_PAGE_WDXCQ);
			mv.addObject("user", user);
			mv.addObject("mobile",user.getPhoneNum());
			if(!StringUtil.isNullOrEmpty(car)){
				SessionHelper.setAttribute(request, MobileContants.CAR_SESSION_KEY, car);
			}
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}

		return mv;
	}
	/**
	 * 购买洗车券
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pay/buycouponPage.html", method = RequestMethod.GET)
	public ModelAndView buycoupon(HttpServletRequest request,
								  HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		//获取当前登录的用户id
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			mv.addObject("mobile",user.getPhoneNum());
			String carNo="";
			Car car=(Car) SessionHelper.getAttribute(request, MobileContants.CAR_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(car)){
				carNo=car.getCarNo();
				/*if(StringUtil.isNullOrEmpty(carNo)){
					carNo=user.getAccount();
				}*/
			}/*else{
				carNo=user.getAccount();
			}*/
			mv.addObject("carNo",carNo);//车牌号

			mv.setViewName(MobilePageContants.FM_PAGE_GMXCQ);
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}

		return mv;
	}
	/**
	 * 我的洗车订单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/myorderlist.html", method = RequestMethod.GET)
	public ModelAndView bmsuccess(HttpServletRequest request,
								  HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		//获取当前登录的用户id
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			mv.addObject("mobile",user.getPhoneNum());
			mv.addObject("user",user);
			mv.setViewName(MobilePageContants.FM_LZH);
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}
		return mv;
	}
	/**
	 * 洗车工登陆
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/adminLogin.html", method = RequestMethod.GET)
	public ModelAndView adminLogin(HttpServletRequest request,
								   HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.ADMIN_LOGIN_PAGE);
		return mv;
	}
	/**
	 * 积分签到
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/jfqd.html", method = RequestMethod.GET)
	public ModelAndView qiandao(HttpServletRequest request,
								HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.QIANDAO_PAGE);
		return mv;
	}
	@RequestMapping(value = "/washcar/main.html", method = RequestMethod.GET)
	public ModelAndView indexMain(HttpServletRequest request,
							  HttpServletResponse response, ModelMap model,String couponId,String carType) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.PAGE_MAIN);
		return mv;
	}
	/**
	 * 选择车型
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectCarType.html", method = RequestMethod.GET)
	public ModelAndView selectCarType(HttpServletRequest request,
									  HttpServletResponse response, ModelMap model,Car car ) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.SELECT_CAR_TYPE_PAGE);
		//mv.addObject("bo",car);//保存刚刚录入的车辆信息
		SessionHelper.setAttribute(request,MobileContants.CAR_SESSION_KEY,car);
		return mv;
	}
	/**
	 * 选择车型
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/selectCarType.html", method = RequestMethod.POST)
	public ModelAndView selectCarTypeForPost(HttpServletRequest request,
											 HttpServletResponse response, ModelMap model,Car car ) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.SELECT_CAR_TYPE_PAGE);
		SessionHelper.setAttribute(request,MobileContants.CAR_SESSION_KEY,car);
		return mv;
	}

	/**
	 * 选择服务项目
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fwxm.html", method = RequestMethod.GET)
	public ModelAndView selectServiceProjectPage(HttpServletRequest request,
												 HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.SELECT_SERVICE_PROJECT_PAEG);
		return mv;
	}
	/**
	 * 重置密码
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/resetPassword.html", method = RequestMethod.GET)
	public ModelAndView resetPassword(HttpServletRequest request,
									  HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.RESET_PASSWORD_PAGE);
		return mv;
	}
	/**
	 * qq客服
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/qq.html", method = RequestMethod.GET)
	public ModelAndView qqKefuPage(HttpServletRequest request,
									  HttpServletResponse response, ModelMap model) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		mv.setViewName("mobile/qq/qq");
		return mv;
	}

	/**
	 * 单页资源请求处理
	 */
	@RequestMapping(value = "/url/**/*.*", method = RequestMethod.GET)
	public String urlReq(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String filePath = URLHelper.getFilePath(request);
		String sPath = "";

		if(FileHelper.isHaveFile(filePath))
		{
			sPath =  FmUtils.fmHtmlPage(request, model, URLHelper.getStatePath(request));
		}
		else
		{
			sPath = FmUtils.fmNotFountPage(request, response, model);
		}
		return sPath;
	}
	@Autowired
	protected IHistoryCarService historyCarService ;
	@Autowired
	protected IWashCouponService washCouponService;
	@Autowired
	protected ICoreService coreService;

}
