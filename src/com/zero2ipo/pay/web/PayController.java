package com.zero2ipo.pay.web;

import com.zero2ipo.common.entity.*;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.core.MobileUserContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IAddressService;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.bsb.IWashCouponService;
import com.zero2ipo.mobile.services.config.IConfManage;
import com.zero2ipo.mobile.utils.DateUtil;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.pay.model.MdlPay;
import com.zero2ipo.pay.service.WXPay;
import com.zero2ipo.pay.service.WXPrepay;
import com.zero2ipo.pay.util.OrderUtil;
import com.zero2ipo.pay.wxsmpay.demo.Demo;
import com.zero2ipo.weixin.services.message.ICoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import weibo4j.org.json.JSONException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class PayController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService ;
	@Resource(name = "orderService")
	private IOrderService orderService;
	@Resource(name = "addressService")
	private IAddressService addressService;
	@Resource(name = "washCouponService")
	private IWashCouponService washCouponService;
	@Resource(name = "coreService")
	private ICoreService coreService;
	@Resource(name = "confManage")
	private IConfManage confManage;
	//通知回调地址
	String notifyUrl = "/pay/update";
	String coupon_notifyUrl="/pay/couponUpdate";
	public PayController(){

	}
	@RequestMapping("/pay/testpage.html")
	public ModelAndView payTestpage(HttpServletRequest request, HttpServletResponse response,String appId,String partnerId,String partnerKey, ModelMap model) {
	    //FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView("pay/index");
		return mv;
	}
	@RequestMapping("/pay/couponIndex.html")
	public ModelAndView couponIndex(HttpServletRequest request, HttpServletResponse response,String appId,String partnerId,String partnerKey, ModelMap model) {
	    //FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView("pay/coupon");
		return mv;
	}
	@RequestMapping("/pay/money.html")
	public ModelAndView paymoney(HttpServletRequest request, HttpServletResponse response,String appId,String partnerId,String partnerKey, ModelMap model) {
	    //FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView("pay/moneypay");
		return mv;
	}

	/**
	 * 扫码支付页面
	 * @param request
	 * @param response
	 * @param appId
	 * @param partnerId
	 * @param partnerKey
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/sm.html")
	public ModelAndView smPage(HttpServletRequest request, HttpServletResponse response,String appId,String partnerId,String partnerKey, ModelMap model) {
		FmUtils.FmData(request, model);
		String codeUrl=Demo.getCodeUrl();
		ModelAndView mv=new ModelAndView("pay/page/native_demo");
		mv.addObject("codeUrl",codeUrl);
		return mv;
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/pay/prepay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> prepayAjax(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map,String total) {
		Map<String,Object> result=new HashMap<String, Object>();
		String spbill_create_ip = request.getRemoteAddr();
		MdlPay pay = getMdlPay();
		model.addAttribute("pay", pay);
		WXPrepay prePay = new WXPrepay();
		String partnerKey=coreService.getValue(CodeCommon.PartnerKey);
		String appid=coreService.getValue(CodeCommon.APPID);
		prePay.setAppid(appid);
		String prePayBody=coreService.getValue(CodeCommon.PREPAY_BODY);
		prePay.setBody(prePayBody);
		String  partnerValue=coreService.getValue(CodeCommon.PartnerValue);
		prePay.setPartnerKey(partnerValue);
		prePay.setMch_id(partnerKey);
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		prePay.setOpenid(openid);
		String jsParam="";
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		logger.info("获取的预支付订单是：" + prepayid);
		if (!"".equals(prepayid)&&prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
			System.out.println("jsParam=" + jsParam);
			// 此处可以添加订单的处理逻辑
			model.addAttribute("jsParam", jsParam);
			result.put("jsParam", jsParam);
			result.put("prepayid", prepayid);
			logger.info("生成的微信调起JS参数为：" + jsParam);
			Date date = new Date();
			DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

			String formattedDate = dateFormat.format(date);
			result.put("status", 1);
			result.put("serverTime", formattedDate );
		}
		return result;
	}
	/**
	 * 获取并填写支付参数
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/config")
	public String config( ModelMap model,String total,String ids,HttpServletRequest request,RedirectAttributes attr,Locale locale,String id,String addressId) {
		String spbill_create_ip = request.getRemoteAddr();
		MdlPay pay = getMdlPay();
		model.addAttribute("pay", pay);
		WXPrepay prePay = new WXPrepay();
		String partnerKey=coreService.getValue(CodeCommon.PartnerKey);
		String appid=coreService.getValue(CodeCommon.APPID);
		prePay.setAppid(appid);
		String prePayBody=coreService.getValue(CodeCommon.PREPAY_BODY);
		prePay.setBody(prePayBody);
		String  partnerValue=coreService.getValue(CodeCommon.PartnerValue);
		prePay.setPartnerKey(partnerValue);
		prePay.setMch_id(partnerKey);
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		float total_free=Float.parseFloat(total);
		float  b   =  (float)(Math.round(total_free*100))/100;
		prePay.setTotal_fee((int)(b*100)+"");
		prePay.setTrade_type("JSAPI");
		String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openid)){
			openid=MobileUserContants.DEFAULT_OPEN_ID;
		}
		prePay.setOpenid(openid);
		String jsParam="";
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		//根据id查询车辆信息
		Map<String,Object> queryMap=new HashMap<String,Object>();
		queryMap.put("id", id);
		Car car=historyCarService.findById(queryMap);
		//根据id查询地址
		Address address=addressService.findById(addressId);
		car.setAddress(address);
		attr.addFlashAttribute("car",car);
		attr.addFlashAttribute("total",total);
		String orderTime=DateUtil.getCurrentDateStr();
		attr.addFlashAttribute("orderTime", orderTime);
		attr.addFlashAttribute("serverTime", formattedDate );
		String orderNo=DateUtil.getDateOrderNo();
		attr.addFlashAttribute("orderNo", orderNo );

		if(!StringUtil.isNullOrEmpty(ids)){
			String projectNames=historyCarService.getServiceProjectNames(ids);
			//根据ids查询服务项目
			attr.addFlashAttribute("serviceProjectName", projectNames );
		}
		//生成订单
		Order order=new Order();
		order.setCarNum(car.getCarNo());
		order.setCarColor(car.getCarColor());
		order.setCarType(car.getCarType());
		order.setOrderId(orderNo);
		order.setCreateTime(orderTime);
		order.setWashTime(car.getPreTime());

		order.setGoodsId(ids);
		order.setAddress(address.getWashAddr());
		order.setOrderStatus(MobileContants.ORDER_PAY_STATUS_DEFAULT);
		order.setPrice(Float.parseFloat(total));
		order.setUserId(openid);
		//获取当前登陆的用户
		Users u= (Users) SessionHelper.getAttribute(request,MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(u)){
			order.setMobile(u.getPhoneNum());
			order.setUserId(u.getUserId());
		}

		if (!"".equals(prepayid)&&prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
			// 此处可以添加订单的处理逻辑
			model.addAttribute("jsParam", jsParam);
			attr.addFlashAttribute("jsParam", jsParam);
			attr.addFlashAttribute("prepayid", prepayid);
			attr.addFlashAttribute("notifyUrl",notifyUrl);
			logger.info("生成的微信调起JS参数为：" + jsParam);

		}
		order.setJsParam(jsParam);
		int orderId=orderService.add(order);
		attr.addFlashAttribute("orderId",orderId);
		attr.addFlashAttribute("prepayid", prepayid);
		attr.addFlashAttribute("jsParam", jsParam);
		attr.addFlashAttribute("notifyUrl", notifyUrl);
		return "redirect:/pay/testpage.html";
	//}
		//return "forward:wxpay";
	}

	/**
	 * 获取微信支付参数
	 * appId 微信appid
	 * partnerId 微信支付商户号
	 * partnerkey 微信支付商户秘钥
	 * @return
	 */
	public MdlPay getMdlPay() {
		MdlPay pay = new MdlPay();
		String partnerId=coreService.getValue(CodeCommon.PartnerKey);
		String appid=coreService.getValue(CodeCommon.APPID);
		//String prePayBody=coreService.getValue(CodeCommon.PREPAY_BODY);
		String  partnerValue=coreService.getValue(CodeCommon.PartnerValue);
		pay.setAppId(appid);
		pay.setPartnerId(partnerId);
		pay.setPartnerKey(partnerValue);
		System.out.println("appid========" + appid);
		System.out.println("partnerId========" + partnerId);
		System.out.println("partnerValue========"+partnerValue);
		return pay;
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
	 * 购买洗车券
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/washCouponbuy.html")
	public String couponbuy( ModelMap model,String total,HttpServletRequest request,RedirectAttributes attr,Locale locale,String id) {
		String spbill_create_ip = request.getRemoteAddr();
		MdlPay pay = getMdlPay();
		model.addAttribute("pay", pay);
		WXPrepay prePay = new WXPrepay();
		//String partnerKey=coreService.getValue(CodeCommon.PartnerKey);
		//String appid=coreService.getValue(CodeCommon.APPID);
		String prePayBody=coreService.getValue(CodeCommon.PREPAY_BODY);
		//String  partnerValue=coreService.getValue(CodeCommon.PartnerValue);
		prePay.setAppid(pay.getAppId());
		prePay.setBody(prePayBody);
		prePay.setPartnerKey(pay.getPartnerKey());
		prePay.setMch_id(pay.getPartnerId());
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		float total_free=Float.parseFloat(total);
		float  b   =  (float)(Math.round(total_free*100))/100;
		prePay.setTotal_fee((int)(b*100)+"");
		prePay.setTrade_type("JSAPI");
		String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		prePay.setOpenid(openid);
		String jsParam="";
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		System.out.println("prepayid2======"+prepayid);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		//根据id查询车辆信息
		Map<String,Object> queryMap=new HashMap<String,Object>();

		attr.addFlashAttribute("total",total);
		String orderTime=DateUtil.getCurrentDateStr();
		attr.addFlashAttribute("orderTime", orderTime);
		attr.addFlashAttribute("serverTime", formattedDate );
		String orderNo=DateUtil.getDateOrderNo();
		attr.addFlashAttribute("orderNo", orderNo );


		//生成洗车券和用对应关系表
		//UserCoupon coupon=new UserCoupon();
		//coupon.setUserId(openid);
		//coupon.setCouponId(id);
		if (!"".equals(prepayid)&&prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
			System.out.println("jsParam=" + jsParam);
			// 此处可以添加订单的处理逻辑
			model.addAttribute("jsParam", jsParam);
			attr.addFlashAttribute("jsParam", jsParam);
			attr.addFlashAttribute("prepayid", prepayid);
			attr.addFlashAttribute("notifyUrl",coupon_notifyUrl);
			logger.info("生成的微信调起JS参数为：" + jsParam);

		}

		attr.addFlashAttribute("prepayid", prepayid);
		attr.addFlashAttribute("couponId", id);
		attr.addFlashAttribute("jsParam", jsParam);
		attr.addFlashAttribute("notifyUrl", notifyUrl);
		return "redirect:/pay/couponIndex.html";
	//}
		//return "forward:wxpay";
	}
	/**
	 * 购买洗车券主方法
	 * @param model
	 * @return
	 * @throws JSONException
	 */
	@RequestMapping("/pay/washCouponbuyAjax.html")
	@ResponseBody
	public Map<String,Object> couponbuyAjax( ModelMap model,String total,HttpServletRequest request,RedirectAttributes attr,Locale locale,String id) throws JSONException {
		Map<String,Object> resultMap=new HashMap<String,Object>();
		String prePayBody=coreService.getValue(CodeCommon.PREPAY_BODY);
		MdlPay pay = getMdlPay();
		model.addAttribute("pay", pay);
		String spbill_create_ip = request.getRemoteAddr();
		WXPrepay prePay = new WXPrepay();
		prePay.setAppid(pay.getAppId());
		prePay.setBody(prePayBody);
		prePay.setPartnerKey(pay.getPartnerKey());
		prePay.setMch_id(pay.getPartnerId());
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		float total_free=Float.parseFloat(total);
		float  b   =  (float)(Math.round(total_free*100))/100;
		prePay.setTotal_fee((int)(b*100)+"");
		prePay.setTrade_type("JSAPI");
		String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
		if(StringUtil.isNullOrEmpty(openid)){
			openid="oGUY1ww1SgBsyTCVeUhtKbI4HIl0";
		}
		prePay.setOpenid(openid);
		String jsParam="";
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		System.out.println("prepayid3======"+prepayid);
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);
		//根据id查询车辆信息
		Map<String,Object> queryMap=new HashMap<String,Object>();

		attr.addFlashAttribute("total",total);
		String orderTime=DateUtil.getCurrentDateStr();
		attr.addFlashAttribute("orderTime", orderTime);
		attr.addFlashAttribute("serverTime", formattedDate );
		String orderNo=DateUtil.getDateOrderNo();
		attr.addFlashAttribute("orderNo", orderNo );


		//生成洗车券和用对应关系表
		//UserCoupon coupon=new UserCoupon();
		//coupon.setUserId(openid);
		//coupon.setCouponId(id);
		if (!"".equals(prepayid)&&prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(pay.getAppId(),pay.getPartnerKey(), prepayid);
			System.out.println("jsParam=" + jsParam);
			// 此处可以添加订单的处理逻辑
			model.addAttribute("jsParam", jsParam);
			attr.addFlashAttribute("jsParam", jsParam);
			attr.addFlashAttribute("prepayid", prepayid);
			attr.addFlashAttribute("notifyUrl",coupon_notifyUrl);
			logger.info("生成的微信调起JS参数为：" + jsParam);

		}
		resultMap.put("prepayid", prepayid);
		resultMap.put("couponId", id);
		resultMap.put("jsParam", jsParam);
		resultMap.put("notifyUrl", notifyUrl);
		return resultMap;
	//}
		//return "forward:wxpay";
	}

	/**
	 * 微信支付主方法
	 * @param request
	 * @param pay
	 * @param model
	 * @return
	 */
	@RequestMapping("/pay/wxpay")
	public String pay(HttpServletRequest request,MdlPay pay, ModelMap model,RedirectAttributes attrs) {
		FmUtils.FmData(request, model);
		String spbill_create_ip = request.getRemoteAddr();
		WXPrepay prePay = getWxPrepay(pay, spbill_create_ip);
		String jsParam="";
        //此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		logger.info("获取的预支付订单是：" + prepayid);
		if (!"".equals(prepayid)&&prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
			System.out.println("jsParam=" + jsParam);
			// 此处可以添加订单的处理逻辑
			model.addAttribute("jsParam", jsParam);
			logger.info("生成的微信调起JS参数为：" + jsParam);
		}
		attrs.addAttribute("prepayid", prepayid);
		attrs.addAttribute("jsParam", jsParam);
		return "redirect:/pay/testpage.html";
	}

	/**
	 * 获取微信通用支付参数
	 * @param pay
	 * @param spbill_create_ip
	 * @return
	 */
	public WXPrepay getWxPrepay(MdlPay pay, String spbill_create_ip) {
		WXPrepay prePay = new WXPrepay();
		prePay.setAppid(pay.getAppId());
		prePay.setBody("来自Sunlight的微信支付测试");
		prePay.setPartnerKey(pay.getPartnerKey());
		prePay.setMch_id(pay.getPartnerId());
		prePay.setNotify_url(notifyUrl);
		prePay.setOut_trade_no(OrderUtil.GetOrderNumber(""));
		prePay.setSpbill_create_ip(spbill_create_ip);
		prePay.setTotal_fee("1");
		prePay.setTrade_type("JSAPI");
		return prePay;
	}

	/**
	 * 根据订单id查询订单详情页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pay/order{id}.html", method = RequestMethod.GET)
	public ModelAndView orderdetail(HttpServletRequest request, HttpServletResponse response, Model model,@PathVariable("id") String id) {
		ModelAndView mv=new ModelAndView("mobile/user/orderdetail");
		//ModelAndView mv=new ModelAndView("pay/index");
		Map<String,Object> queryMap=new HashMap<String, Object>();
		queryMap.put("id", id);
		Order order=orderService.findById(queryMap);
		if(!StringUtil.isNullOrEmpty(order)){
			mv.addObject("order",order);
		}

		return mv;
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/pay/notice_url", method = RequestMethod.GET)
	public ModelAndView updateOrderNotice(HttpServletRequest request, HttpServletResponse response, Model model,ModelMap map,Order order) {
		Map<String,Object> result=new HashMap<String, Object>();
		boolean flag=orderService.update(order);
		result.put("id", order.getId());
		order=orderService.findById(result);
		ModelAndView mv=new ModelAndView("mobile/user/orderdetail");
		Map<String,Object> queryMap=new HashMap<String, Object>();
		System.out.println("支付结果======================="+flag);
		if(!StringUtil.isNullOrEmpty(order)){
			mv.addObject("order",order);
		}


		return mv;
	}
	//微信支付回调函数
	@RequestMapping(value = "/pay/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateOrder(HttpServletRequest request, HttpServletResponse response, Model model,ModelMap map,Order order) {
		Map<String,Object> result=new HashMap<String, Object>();
		boolean flag=orderService.update(order);
		System.out.println("支付结果======================="+flag);
		result.put("orderId", order.getId());
		return result;
	}
	/**
	 * 优惠券支付回调函数
	 * @param request
	 * @param response
	 * @param model
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/pay/couponUpdate", method = RequestMethod.GET)
	public ModelAndView couponUpdate(HttpServletRequest request, HttpServletResponse response, Model model,ModelMap map,String couponId) {
		//返回到我的洗车券列表
		ModelAndView mv=new ModelAndView(MobilePageContants.FM_PAGE_WDXCQ);
		FmUtils.FmData(request, map);
		Users userEntity=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		boolean flag=false;
		String userId="";
		String carNo="";
		if(StringUtil.isNullOrEmpty(userEntity)){
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}else{
			userId=userEntity.getUserId();
 			mv.addObject("mobile", userEntity.getPhoneNum());
			mv.addObject("carNo",carNo);
			System.out.println("couponId======="+couponId);
			if(!StringUtil.isNullOrEmpty(couponId)){
				//根据douponId查询洗车券
				Map<String,Object> queryMap=new HashMap<String, Object>();
				queryMap.put("id", couponId);
				GgwashCoupon washCoupon=null;
				washCoupon=washCouponService.findById(queryMap);
				if(!StringUtil.isNullOrEmpty(washCoupon)){
					UserCoupon userCoupon=null;
					//车牌号作为唯一查询是否购买优惠券的条件
					//queryMap.put("carNo", carNo);
					//手机号码作为唯一标识
					queryMap.put("userId",userEntity.getPhoneNum());
					//System.out.println("查询参数======"+carNo);
					//根据手机号码查询此用户是否购买过此洗车券
					userCoupon=washCouponService.findUserCouponById(queryMap);
					if(!StringUtil.isNullOrEmpty(userCoupon)&&!StringUtil.isNullOrEmpty(userCoupon.getCouponId()))
					{
						System.out.println("id======="+userCoupon.getId());
						userCoupon.setNumber(userCoupon.getNumber()+washCoupon.getNumber());
						userCoupon.setUserId(userId);
						//userCoupon.setCarNo(userEntity.g);
						//更新洗车券数量
						washCouponService.updateCouponNum(userCoupon);
						System.out.println("更新洗车券==========");
					}else{
						//新增
						userCoupon=new UserCoupon();
						userCoupon.setCouponId(couponId);
						//userCoupon.setCarNo(userEntity.getAccount());
						userCoupon.setUserId(userId);
						userCoupon.setNumber(washCoupon.getNumber());
						//userCoupon.setUserName(userEntity.get);
						flag=orderService.updateCoupon(userCoupon);
						System.out.println("新增洗车券-==========================================="+flag);
					}
				}
			}
		}
		System.out.println("保存结果======================="+flag);
		mv.addObject("success", flag);
		mv.addObject("openid",userId);
		mv.addObject("user", userEntity);

		return mv;
	}
}
