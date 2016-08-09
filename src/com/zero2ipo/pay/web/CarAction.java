package com.zero2ipo.pay.web;

import com.zero2ipo.SDK.config.AppConfig;
import com.zero2ipo.SDK.lib.MESSAGEXsend;
import com.zero2ipo.SDK.utils.ConfigLoader;
import com.zero2ipo.car.vipcoupon.bizc.IVipCouponService;
import com.zero2ipo.car.vipcoupon.bo.VipCoupon;
import com.zero2ipo.common.entity.*;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.*;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.mobile.utils.DateUtil;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.pay.model.MdlPay;
import com.zero2ipo.pay.service.WXPay;
import com.zero2ipo.pay.service.WXPrepay;
import com.zero2ipo.pay.util.OrderUtil;
import com.zero2ipo.weixin.services.message.ICoreService;
import com.zero2ipo.weixin.templateMessage.TemplateMessageUtils;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import com.zero2ipo.weixin.utils.GetAccessTokenUtil;
import com.zero2ipo.weixin.utils.Sign;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.StringReader;
import java.util.*;

/**
 * Created by Administrator on 2015/8/31.
 */
@Controller
public class CarAction {
	String notifyUrl = "/order/wxpayHdMethod.html";//微信支付成功回调方法,修改订单状态以及自动派单
	/**
	 * 订单流程页面
	 * */
	@RequestMapping(value = "/f/order{id}.html")
	public ModelAndView selectServicePage(HttpServletRequest request, HttpServletResponse response, ModelMap model,@PathVariable("id") String id,String flag)
	{
		ModelAndView mv=new ModelAndView(MobilePageContants.CAR_DETAIL_PAGE);
		FmUtils.FmData(request, model);
		mv.addObject("orderId", id);
		mv.addObject("flag",flag);
		return mv;
	}
	/**
	 * 洗车工修改任务界面
	 * */
	@RequestMapping(value = "/renwu/order{id}.html")
	public ModelAndView adminOderDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model,@PathVariable("id") String id,RedirectAttributes  attr)
	{
		ModelAndView mv=new ModelAndView(MobilePageContants.ADMIN_ORDER_DETAIL_PAGE);
		FmUtils.FmData(request, model);
		ServletContext application =request.getSession().getServletContext();

		//首先从缓存中获取appid
		String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
		String appSecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
		//AccessToken token=TokenThread.accessToken;
		//String access_token = token.getToken();

		String access_token =GetAccessTokenUtil.getAccess_token2(appId,appSecret);
		System.out.println("appid================================================"+appId+"\t"+appSecret);
		System.out.println("access_token======================================"+access_token);
		sweepParam(request,mv,appId,access_token);
		mv.addObject("orderId", id);
		return mv;
	}
	public static void sweepParam(HttpServletRequest request, ModelAndView mv,String appId,String access_token) {
		String url=request.getRequestURL().toString();
		System.out.println("url======================"+url);
		Map<String, String> res= Sign.getConfigMessageForWater(url,access_token);
		mv.addObject("url",url);
		mv.addObject("appid",appId);
		mv.addObject("timestamp",res.get("timestamp"));
		mv.addObject("nonceStr",res.get("nonceStr"));
		mv.addObject("signature",res.get("signature"));
	}
	/**
	 * 洗车工修改任务界面
	 * */
	@RequestMapping(value = "/renwu/order{id}/f{flag}.html")
	public ModelAndView updateSendOrderSuccessPage(HttpServletRequest request, HttpServletResponse response, ModelMap model,@PathVariable("id") String id,@PathVariable("flag") String flag,RedirectAttributes  attr)
	{
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView(MobilePageContants.ADMIN_ORDER_DETAIL_PAGE);
		//如果没有登录的话，则跳转到洗车工登录页面
		AdminBo adminBo= (AdminBo) SessionHelper.getAttribute(request,MobileContants.ADMIN_SESSION_KEY);
		if(StringUtil.isNullOrEmpty(adminBo)){
			mv.setViewName(MobilePageContants.ADMIN_LOGIN_PAGE);
		}
		mv.addObject("orderId", id);
		mv.addObject("flag", flag);
		return mv;
	}
	/**
	 * 我的订单
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/my/order{id}.html")
	public ModelAndView myOrderPage(HttpServletRequest request, HttpServletResponse response, ModelMap model,@PathVariable("id") String id)
	{
		FmUtils.FmData(request, model);
		//获取当前登录的用户id
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		ModelAndView mv=new ModelAndView();
		if(!StringUtil.isNullOrEmpty(user)){
			//判断此用户的下单次数,如果是首次下单，那么跳转到大转盘抽奖页面，否则跳转到订单详情页面
			String userId=user.getUserId();
			Map<String,Object> queryMap=new HashMap<String, Object>();
			queryMap.put("userId",userId);
			queryMap.put("status",MobileContants.status_1);//限制是成功的订单
			int count=orderService.findIsOrNotFirstOrder(queryMap);
			System.out.println("洗车次数======================"+count);
			if(count<=1){
				mv.setViewName(MobilePageContants.DA_ZHUAN_PAN_PAGE);
			}else{
				mv.setViewName(MobilePageContants.MY_ORDER_PAGE);
				//首先从缓存中获取order
				ServletContext application =request.getSession().getServletContext();
				Order order= (Order) application.getAttribute(MobileContants.CURRENT_ORDER_KEY);
				if(StringUtil.isNullOrEmpty(order)){
					//如过缓存中不存在再去查询数据库
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id",id);
					order=orderService.findById(map);
				}
				mv.addObject("order",order);
			}
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}

		return mv;
	}
	@RequestMapping(value = "/car", method = RequestMethod.GET)
	public ModelAndView carList(HttpServletRequest request,
								HttpServletResponse response, ModelMap model, @PathVariable String templateCode) {
		FmUtils.FmData(request, model);
		List<Car> list=null;
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			if(!StringUtil.isNullOrEmpty(openId)){
				queryMap.put("openId",openId);
				list=historyCarService.findAllList(queryMap);
			}else{
				list=new ArrayList<Car>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("carList",list);
		return mv;
	}

	@RequestMapping(value = "/car/wait_washer.html", method = RequestMethod.GET)
	public String add(HttpServletRequest request,
					  HttpServletResponse response, ModelMap model,@PathVariable Car car){

		Car article=null;
		int primkey=-1;
		FmUtils.FmData(request, model);
		if (!StringUtil.isNullOrEmpty(car)){
			String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			car.setUserCarId(openid);
			primkey= historyCarService.add(car);
		}
		//获取当前登陆的用户

		return MobilePageContants.CAR_DETAIL_PAGE;

	}
	@RequestMapping(value = "/car/wait_washer.html", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, HttpServletResponse response, ModelMap model,@ModelAttribute Car car,RedirectAttributes redirectAttributes){

		boolean flag=false;
		FmUtils.FmData(request, model);
		if (!StringUtil.isNullOrEmpty(car)){
			String openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			car.setUserCarId(openid);
			//首页判断此车辆是否存在
			Map<String,Object> queryMap=new HashMap<String,Object>();
			queryMap.put("carNo", car.getCarNo());

			queryMap.put("carSeats", car.getCarSeats());
			queryMap.put("carColor", car.getCarColor());
			Car isExsit=historyCarService.findById(queryMap);
			int cid;//c车辆id
			String openId=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			if(StringUtil.isNullOrEmpty(openId)){
				openId=MobileContants.DEFAULT_OPEN_ID;
			}
			car.setUserCarId(openId);
			if(StringUtil.isNullOrEmpty(isExsit)){
				car.setCarColor(car.getCarColor().replace(",",""));
				car.setCarSeats(car.getCarSeats().replace(",", ""));
				int id= historyCarService.add(car);//新增
				cid=id;
				redirectAttributes.addFlashAttribute("id", id);
				if(id>0){
					flag=true;
				}
			}else{//更新
				cid=isExsit.getId();

        		/*queryMap.put("userCardId", openId);
        		isExsit.setWashAddr(car.getWashAddr());
        		isExsit.setWashInfo(car.getWashInfo());
        		isExsit.setMobile(car.getMobile());
        		isExsit.setCarColor(car.getCarColor().replace(",",""));
        		isExsit.setCarSeats(car.getCarSeats().replace(",", ""));
        		isExsit.setPreTime(car.getPreTime());
        		flag=historyCarService.update(isExsit);
        		redirectAttributes.addFlashAttribute("id", isExsit.getId());*/
			}
			//更新车辆地址
			Address address=new Address();
			address.setCid(cid);
			address.setMobile(car.getMobile());
			address.setWashAddr(car.getWashAddr());
			address.setOpenId(openId);
			int addressId=addressService.add(address);
			if(addressId>0){
				flag=true;
			}
			redirectAttributes.addFlashAttribute("carId", cid);
			redirectAttributes.addFlashAttribute("addressId", addressId);
		}
		return "redirect:/car/selectService.html";

	}
	@RequestMapping(value = "/car/selectService.html", method = RequestMethod.GET)
	public String addPost(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		FmUtils.FmData(request, model);
		Object object=SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		String page=MobilePageContants.SELECT_SERVICE_PROJECT_PAEG;
		if(!(object instanceof Users)){
			SessionHelper.setAttribute(request, MobileContants.PAGE_SESSION_KEY,"/car/selectService.html");
			page=MobilePageContants.FM_USER_LOGIN;
		}
		return page;
	}

	/**
	 * 首页下单
	 * @param request
	 * @param response
	 * @param model
	 * @param car
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "/car/xd.html", method = RequestMethod.POST)
	public String xd(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String couponId,String lat,String lng,RedirectAttributes redirectAttributes){

		boolean flag=false;
		FmUtils.FmData(request, model);
		int orderId=-1;
		int carId=-1;
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user))
		{

			if (!StringUtil.isNullOrEmpty(car)){
				car.setUserCarId(user.getUserId());
				//首页判断此车辆是否存在
				Map<String,Object> queryMap=new HashMap<String,Object>();
				queryMap.put("mobile",user.getPhoneNum());
				Car isExsit=null;
				List<Car> historyCar=historyCarService.findAllList(queryMap);
				if(historyCar.size()>0){
					isExsit=historyCar.get(0);
				}
				if(StringUtil.isNullOrEmpty(isExsit)){
					car.setUserCarId(user.getUserId());
					carId= historyCarService.add(car);//新增
					redirectAttributes.addFlashAttribute("id", carId);
					car.setId(carId);
					redirectAttributes.addFlashAttribute("bo", car);
					isExsit=car;
					if(carId>0){
						flag=true;
					}
				}else{//更新
					queryMap.put("userCardId", user.getUserId());
					isExsit.setWashAddr(car.getWashAddr());
					isExsit.setName(car.getName());
					isExsit.setWashInfo(car.getWashInfo());
					isExsit.setMobile(car.getMobile());
					isExsit.setCarColor(car.getCarColor());
					isExsit.setCarSeats(car.getCarSeats());
					isExsit.setCarType(car.getCarType());
					isExsit.setCarNo(car.getCarNo());
					isExsit.setUserCarId(user.getUserId());
					isExsit.setPreTime(car.getPreTime());
					flag=historyCarService.update(isExsit);
					redirectAttributes.addFlashAttribute("id", isExsit.getId());
					redirectAttributes.addFlashAttribute("bo", isExsit);
				}
				/**移除录入的车辆信息保存的session**/
				SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
             	/*生成订单*/
				//生成订单
				Order order=new Order();
				order.setCarNum(isExsit.getCarNo());
				String orderNo=DateUtil.getDateOrderNo();
				order.setOrderId(orderNo);
				String orderTime=DateUtil.getCurrentDateStr();
				order.setCreateTime(orderTime);
				order.setOrderStatus(MobileContants.ORDER_PAY_STATUS_DEFAULT);
				order.setWashTime(isExsit.getPreTime());
				order.setCarNum(isExsit.getCarNo());
				order.setCarType(isExsit.getCarType());
				order.setCarColor(isExsit.getCarColor());
				order.setLat(lat);
				order.setLon(lng);
				order.setCardsId(couponId);
				order.setAddress(isExsit.getWashAddr());
				order.setMobile(user.getPhoneNum());
				order.setDiscription(isExsit.getWashInfo());
				order.setCarId(carId+"");
				//设置洗车券id
				order.setCardsId(couponId);
				//根据 洗车券id查询 洗车券 价格
				Map<String,Object> m=new HashMap<String,Object>();
				m.put("id", couponId);
				GgwashCoupon coupon=washCouponService.findById(m);
				if(!StringUtil.isNullOrEmpty(coupon)){
					order.setPrice(coupon.getPrice());
					order.setWashType(coupon.getName());
				}
				//order.setStatus("-1");
				order.setSendOrderStatus("0"); //默认派单状态
				order.setUserId(user.getUserId());
				//order.setJsParam("");
				orderId=orderService.add(order);
				//如果订单 保存 成功的话 ，减去 用户已经使用 的洗车券
				System.out.println("支付成功后orderid="+orderId);
				if(orderId>1){
					m.put("userId", user.getUserId());
					System.out.println("开始删除洗车券====================");
					//根据couponId查询UserCoupon
					UserCoupon userCoupon=washCouponService.findUserCouponById(m);
					if(!StringUtil.isNullOrEmpty(userCoupon)&&userCoupon.getNumber()>0){//减少次数
						userCoupon.setNumber(userCoupon.getNumber()-1);
						washCouponService.updateCouponNum(userCoupon);
					}else{//直接删除
						//washCouponService.delUserCouponById(couponId);
					}

				}
				order.setOrderId(orderId+"");
				redirectAttributes.addFlashAttribute("order",order);
				redirectAttributes.addFlashAttribute("orderId",orderId);
				redirectAttributes.addFlashAttribute("mobile",user.getPhoneNum());
             	/*洗车券减少**/
			}
		}



		return "redirect:/my/order"+orderId+".html";

	}
	/**
	 * 首页下单Ajax 洗车券下单
	 * @param request
	 * @param response
	 * @param model
	 * @param car
	 * @return
	 */
	@RequestMapping(value = "/xd.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> xdAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String couponId,String lat,String lng){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		boolean flag=false;
		FmUtils.FmData(request, model);
		int orderId=-1;
		int carId=-1;
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user))
		{

			if (!StringUtil.isNullOrEmpty(car)){
				car.setUserCarId(user.getUserId());
				//首页判断此车辆是否存在
				Map<String,Object> queryMap=new HashMap<String,Object>();
				queryMap.put("carNo",car.getCarNo());
				Car isExsit=null;
				int count=historyCarService.findAllListCount(queryMap);
				if(count<=0){
					carId= historyCarService.add(car);//新增
					car.setId(carId);
					isExsit=car;
					if(carId>0){
						flag=true;
						isExsit.setId(carId);
					}
				}else{//更新
					queryMap.put("userCardId", user.getUserId());
					isExsit.setWashAddr(car.getWashAddr());
					isExsit.setName(car.getName());
					isExsit.setWashInfo(car.getWashInfo());
					isExsit.setMobile(car.getMobile());
					isExsit.setCarColor(car.getCarColor());
					isExsit.setCarSeats(car.getCarSeats());
					isExsit.setCarType(car.getCarType());
					isExsit.setCarNo(car.getCarNo());
					isExsit.setPreTime(car.getPreTime());
					flag=historyCarService.update(isExsit);
					carId=isExsit.getId();
				}
				/**移除录入的车辆信息保存的session**/
				SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
             	/*生成订单*/
				//生成订单
				Order order=new Order();
				//order.setCid(isExsit.getId());
				String orderNo=DateUtil.getDateOrderNo();
				order.setOrderId(orderNo);
				String orderTime=DateUtil.getCurrentDateStr();
				order.setCreateTime(orderTime);
				order.setOrderStatus(MobileContants.ORDER_PAY_STATUS_DEFAULT);
				order.setWashTime(car.getPreTime());
				order.setCarNum(car.getCarNo());
				order.setCarColor(car.getCarColor());
				order.setAddress(car.getWashAddr());
				order.setMobile(user.getPhoneNum());
				order.setUserId(user.getUserId());
				order.setUserName(car.getName());
				order.setAddress(car.getWashAddr());
				order.setCarType(car.getCarType());
				order.setGoodsId(couponId);
				order.setDiscription(car.getWashInfo());
				order.setCarId(carId+"");
				order.setLon(lng);
				order.setLat(lat);
				//设置洗车券id
				order.setCardsId(couponId);
				order.setLat(lat);
				order.setLon(lng);
				//根据 洗车券id查询 洗车券 价格
				Map<String,Object> m=new HashMap<String,Object>();
				m.put("id", couponId);
				GgwashCoupon coupon=washCouponService.findById(m);
				if(!StringUtil.isNullOrEmpty(coupon)){
					order.setPrice(coupon.getPrice());
					order.setWashType(coupon.getName());
				}
				order.setSendOrderStatus("0");
				order.setUserId(user.getUserId());
				//order.setJsParam("");
				orderId=orderService.add(order);
				//下完单后是否开启自动派单功能
				String autoPaiDan=coreService.getValue(CodeCommon.AUTO_PAIDAN);
				if(CodeCommon.AUTO_PAIDAN_FLAG.equals(autoPaiDan)){
					//派单完成后是否给管理员发送短信或者微信
					ServletContext application =request.getSession().getServletContext();
					isAutoQiangDanMethod(request,application,order);
				}
				//如果订单 保存 成功的话 ，减去 用户已经使用 的洗车券
				if(orderId>1){
					m.put("userId", user.getUserId());
					//根据couponId查询UserCoupon
					UserCoupon userCoupon=washCouponService.findUserCouponById(m);
					if(!StringUtil.isNullOrEmpty(userCoupon)&&userCoupon.getNumber()>0){//减少次数
						userCoupon.setNumber(userCoupon.getNumber()-1);
						washCouponService.updateCouponNum(userCoupon);
					}
				}
				order.setOrderId(orderId+"");
			}
		}
		resultMap.put("success", flag);
		resultMap.put("url", "/my/order"+orderId+".html");
		return resultMap;

	}
	@RequestMapping(value = "/order/qbpay.html", method = RequestMethod.GET)
	public ModelAndView qbpayPage(HttpServletRequest request, HttpServletResponse response, ModelMap model,String orderId) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request, model);
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user))
		{
			Map<String,Object> queryMap=new HashMap<String, Object>();
			queryMap.put("userId",user.getUserId());
			//判断下单次数
			queryMap.put("status",MobileContants.status_1);//限制是成功的订单
			int count=orderService.findIsOrNotFirstOrder(queryMap);
			System.out.println("WACH COUNT========================================"+count);
		/*	if(count<=1){
				mv.setViewName(MobilePageContants.DA_ZHUAN_PAN_PAGE);
			}else{*/
				mv.setViewName(MobilePageContants.PAY_BY_WEIXIN_PAGE);
				Map<String,Object> query=new HashMap<String,Object>();
				query.put("id", orderId);
				mv.addObject("orderId", orderId);
				Order order=orderService.findById(query);
				mv.addObject("order",order);
			/*}*/
		}
		return mv;
	}

	/**
	 * 微信支付--订单详情页面
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
     * @return
     */
	@RequestMapping(value = "/order/wxpay.html", method = RequestMethod.GET)
	public ModelAndView wxpayPage(HttpServletRequest request, HttpServletResponse response, ModelMap model,String orderId) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request, model);
		mv.setViewName(MobilePageContants.PAY_BY_WEIXIN_PAGE);
		Map<String,Object> queryMap=new HashMap<String,Object>();
		queryMap.put("id", orderId);
		mv.addObject("orderId", orderId);
		Order order=orderService.findById(queryMap);
		mv.addObject("order",order);
		return mv;
	}
	/**
	 * 现金支付--订单详情页面
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/washcar/money.html", method = RequestMethod.GET)
	public ModelAndView moneyPayPage(HttpServletRequest request, HttpServletResponse response, ModelMap model,String orderId) {
		ModelAndView mv=new ModelAndView();
		FmUtils.FmData(request, model);
		mv.setViewName(MobilePageContants.PAY_BY_MONEY_PAGE);
		Map<String,Object> queryMap=new HashMap<String,Object>();
		queryMap.put("id", orderId);
		mv.addObject("orderId", orderId);
		Order order=orderService.findById(queryMap);
		mv.addObject("order",order);
		return mv;
	}

	/**
	 * ajax获取微信支付参数
	 * @param request
	 * @param response
	 * @param model
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/order/getJsParam.html", method = RequestMethod.POST)
	@ResponseBody
	public String getJsParam(HttpServletRequest request, HttpServletResponse response, ModelMap model,String orderId) {
		//根据orderId查询订单
		String jsParam="";
		Map<String,Object> queryMap=new HashMap<String, Object>();
		queryMap.put("id",orderId);
		Order order=orderService.findById(queryMap);
		//此时将订单放到缓存中
		ServletContext application =request.getSession().getServletContext();
		application.setAttribute(MobileContants.CURRENT_ORDER_KEY,order);
		if(!StringUtil.isNullOrEmpty(order)){
			//重新生成微信支付参数，防止订单过期
			float toatl=order.getPrice();
			jsParam=getWXJsParamForNative(request,toatl);

		}
		return jsParam;
	}

	/**
	 * 首页下单Ajax 微信支付下单
	 * @param request
	 * @param response
	 * @param model
	 * @param car
	 * @return
	 */
	@RequestMapping(value = "/order/wxpay.html", method = RequestMethod.POST)
	public String wxpayAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String lat,String lng,String totalPrice,String projectName){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		boolean flag=false;
		//FmUtils.FmData(request, model);
		String orderId="";
		int id=0;
		int carId=-1;
		String jsParam="";
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user))
		{

			if (!StringUtil.isNullOrEmpty(car)){
				car.setUserCarId(user.getUserId());
				//首页判断此车辆是否存在
				Map<String,Object> queryMap=new HashMap<String,Object>();
				queryMap.put("mobile",user.getPhoneNum());
				Car isExsit=null;
				List<Car> historyCar=historyCarService.findAllList(queryMap);
				if(historyCar.size()>0){
					isExsit=historyCar.get(0);
				}
				if(StringUtil.isNullOrEmpty(isExsit)){
					carId= historyCarService.add(car);//新增
					car.setId(carId);
					isExsit=car;
					if(carId>0){
						flag=true;
					}
				}else{//更新
					queryMap.put("userCardId", user.getUserId());
					isExsit.setWashAddr(car.getWashAddr());
					isExsit.setName(car.getName());
					isExsit.setWashInfo(car.getWashInfo());
					isExsit.setMobile(car.getMobile());
					isExsit.setCarColor(car.getCarColor());
					isExsit.setCarSeats(car.getCarSeats());
					isExsit.setCarType(car.getCarType());
					isExsit.setCarNo(car.getCarNo());
					isExsit.setPreTime(car.getPreTime());
					flag=historyCarService.update(isExsit);
					carId=isExsit.getId();
				}
				/**移除录入的车辆信息保存的session**/
				SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
             	/*生成订单*/
				//生成订单
				Order order=new Order();
				//order.setCid(isExsit.getId());
				String orderNo=DateUtil.getDateOrderNo();
				order.setOrderId(orderNo);
				String orderTime=DateUtil.getCurrentDateStr();
				order.setCreateTime(orderTime);
				order.setWashTime(car.getPreTime());
				order.setCarNum(car.getCarNo());
				order.setCarColor(car.getCarColor());
				order.setAddress(car.getWashAddr());
				order.setMobile(user.getPhoneNum());
				order.setUserId(user.getUserId());
				order.setUserName(car.getName());
				order.setAddress(car.getWashAddr());
				order.setCarType(car.getCarType());
				order.setDiscription(car.getWashInfo());
				order.setCarId(carId + "");
				order.setPayType(MobileContants.status_1);//微信支付
				order.setOrderStatus(MobileContants.status_fu_1);//现金支付订单状态默认未支付
				order.setLon(lng);
				order.setLat(lat);
				float total_price=0;
				if(!StringUtil.isNullOrEmpty(totalPrice)){
					total_price=Float.parseFloat(totalPrice);
				}
				order.setPrice(total_price);
				order.setWashType(projectName);
				order.setSendOrderStatus(MobileContants.status_0);
				order.setUserId(user.getUserId());
				orderId=OrderUtil.GetOrderNumber("");
				//保存微信jsParam
				jsParam=getWXJsParamForNative(request,total_price);
				order.setJsParam(jsParam);
				order.setOrderId(orderId);
				id=orderService.add(order);
				//下单完成后保存订单主键到缓存中，修改订单状态的时候要用
				order.setId(id);
				ServletContext application =request.getSession().getServletContext();
				application.setAttribute(MobileContants.CURRENT_ORDER_KEY,order);
				System.out.print("保存缓存中的orderid==========================="+order.getId());
			}
		}
		String url="redirect:/order/wxpay.html?orderId="+id;
		return url;

	}
	/**
	 * 首页下单Ajax 现金支付
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/washcar/moneyPay.html", method = RequestMethod.POST)
	public String moneyPay(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String lat,String lng,String totalPrice,String projectName){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		boolean flag=false;
		//FmUtils.FmData(request, model);
		String orderId="";
		int id=0;
		int carId=-1;
		String jsParam="";
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user))
		{

			if (!StringUtil.isNullOrEmpty(car)){
				car.setUserCarId(user.getUserId());
				//首页判断此车辆是否存在
				Map<String,Object> queryMap=new HashMap<String,Object>();
				queryMap.put("carNo",car.getCarNo());
				Car isExsit=null;
				int count=historyCarService.findAllListCount(queryMap);
				if(count<=0){
					carId= historyCarService.add(car);//新增
					car.setId(carId);
					isExsit=car;
					if(carId>0){
						flag=true;
					}
				}else{//更新
					queryMap.put("userCardId", user.getUserId());
					isExsit.setWashAddr(car.getWashAddr());
					isExsit.setName(car.getName());
					isExsit.setWashInfo(car.getWashInfo());
					isExsit.setMobile(car.getMobile());
					isExsit.setCarColor(car.getCarColor());
					isExsit.setCarSeats(car.getCarSeats());
					isExsit.setCarType(car.getCarType());
					isExsit.setCarNo(car.getCarNo());
					isExsit.setPreTime(car.getPreTime());
					flag=historyCarService.update(isExsit);
					carId=isExsit.getId();
				}
				/**移除录入的车辆信息保存的session**/
				SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
             	/*生成订单*/
				//生成订单
				Order order=new Order();
				//order.setCarId(isExsit.getId());
				String orderNo=DateUtil.getDateOrderNo();
				order.setOrderId(orderNo);
				String orderTime=DateUtil.getCurrentDateStr();
				order.setCreateTime(orderTime);
				order.setWashTime(car.getPreTime());
				order.setCarNum(car.getCarNo());
				order.setCarColor(car.getCarColor());
				order.setAddress(car.getWashAddr());
				order.setMobile(user.getPhoneNum());
				order.setUserId(user.getUserId());
				order.setUserName(car.getName());
				order.setAddress(car.getWashAddr());
				order.setCarType(car.getCarType());
				order.setDiscription(car.getWashInfo());
				order.setCarId(carId + "");
				order.setPayType(MobileContants.status_3);//现金支付
				order.setOrderStatus(MobileContants.status_fu_1);//现金支付订单状态默认未支付
				order.setLon(lng);
				order.setLat(lat);
				float total_price=0;
				if(!StringUtil.isNullOrEmpty(totalPrice)){
					total_price=Float.parseFloat(totalPrice);
				}
				order.setPrice(total_price);
				order.setWashType(projectName);
				order.setSendOrderStatus(MobileContants.status_0);
				order.setUserId(user.getUserId());
				orderId=OrderUtil.GetOrderNumber("");
				//保存微信jsParam
				//jsParam=getWXJsParamForNative(request,total_price);
				//order.setJsParam(jsParam);
				order.setOrderId(orderId);
				id=orderService.add(order);
				//下单完成后保存订单主键到缓存中，修改订单状态的时候要用
				order.setId(id);
				ServletContext application =request.getSession().getServletContext();
				application.setAttribute(MobileContants.CURRENT_ORDER_KEY,order);
				System.out.print("保存缓存中的orderid==========================="+order.getId());
			}
		}
		String url="redirect:/washcar/money.html?orderId="+id;
		return url;

	}
	/**
	 * 当钱包充足的时候，直接使用钱包余额抵扣，不走微信支付
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/qbpay.html", method = RequestMethod.POST)
	public String qbpay(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String lat,String lng,String totalPrice,String projectName){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		boolean flag=false;
		//FmUtils.FmData(request, model);
		String orderId="";
		int id=0;
		int carId=-1;
		String jsParam="";
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
			if (!StringUtil.isNullOrEmpty(car)){
				car.setUserCarId(user.getUserId());
				//首页判断此车辆是否存在
				Map<String,Object> queryMap=new HashMap<String,Object>();
				queryMap.put("carNo",car.getCarNo());
				Car isExsit=null;
				int count=historyCarService.findAllListCount(queryMap);
				if(count<=0){
					carId= historyCarService.add(car);//新增
					car.setId(carId);
					isExsit=car;
					if(carId>0){
						flag=true;
					}
				}else{//更新
					queryMap.put("userCardId", user.getUserId());
					isExsit.setWashAddr(car.getWashAddr());
					isExsit.setName(car.getName());
					isExsit.setWashInfo(car.getWashInfo());
					isExsit.setMobile(car.getMobile());
					isExsit.setCarColor(car.getCarColor());
					isExsit.setCarSeats(car.getCarSeats());
					isExsit.setCarType(car.getCarType());
					isExsit.setCarNo(car.getCarNo());
					isExsit.setPreTime(car.getPreTime());
					flag=historyCarService.update(isExsit);
					carId=isExsit.getId();
				}
				//**移除录入的车辆信息保存的session**//
				SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
             	//*生成订单*//
				Order order=new Order();
				String orderNo=DateUtil.getDateOrderNo();
				order.setOrderId(orderNo);
				String orderTime=DateUtil.getCurrentDateStr();
				order.setCreateTime(orderTime);
				order.setWashTime(car.getPreTime());
				order.setCarNum(car.getCarNo());
				order.setCarColor(car.getCarColor());
				order.setAddress(car.getWashAddr());
				order.setMobile(user.getPhoneNum());
				order.setUserId(user.getUserId());
				order.setUserName(car.getName());
				order.setAddress(car.getWashAddr());
				order.setCarType(car.getCarType());
				order.setDiscription(car.getWashInfo());
			order.setCarId(carId + "");
			order.setPayType(MobileContants.status_2);//钱包抵扣
			order.setOrderStatus(MobileContants.status_1);//已付款
			order.setLon(lng);
			order.setLat(lat);
			float total_price=0;
			if(!StringUtil.isNullOrEmpty(totalPrice)){
				total_price=Float.parseFloat(totalPrice);
			}
			order.setPrice(total_price);
			order.setWashType(projectName);
			order.setSendOrderStatus(MobileContants.status_0);
			order.setUserId(user.getUserId());
			orderId=OrderUtil.GetOrderNumber("");
			order.setOrderId(orderId);
			id=orderService.add(order);
			//下单完成后保存订单主键到缓存中，修改订单状态的时候要用
			order.setId(id);
			//保存订单完毕后，减去钱包余额
			float yue=user.getAccount()-total_price;
			user.setAccount(user.getAccount()-total_price);
			userServices.reduceQianBao(user);
			//同时更新缓存
			SessionHelper.removeAttribute(request,MobileContants.USER_SESSION_KEY);
			SessionHelper.setAttribute(request,MobileContants.USER_SESSION_KEY,user);
			request.getSession().removeAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
			request.getSession().setAttribute(MobileContants.USER_APPLICATION_SESSION_KEY,user);
			//接下来是自动派单
			ServletContext application =request.getSession().getServletContext();
			String autoPaiDan=isAutoPaiDanMethod(request, application, order);
			if(CodeCommon.AUTO_PAIDAN_FLAG.equals(autoPaiDan)){
				//判断完毕，更新订单派单状态
				Order o=new Order();
				o.setId(id);
				o.setSendOrderStatus(MobileContants.status_1);
				orderService.updateStatus(o);
			}
			//减少优惠券
			String vipcouponId=order.getVipCouponId();
			if(!StringUtil.isNullOrEmpty(vipcouponId)&&!"null".equals(vipcouponId)){
				//下单的时候使用了优惠券抵扣，所以要把此优惠券状态更改为已使用
				long couponId=Long.parseLong(vipcouponId);
				VipCoupon vipCoupon=new VipCoupon();
				vipCoupon.setId(couponId);
				vipCoupon.setStatus(MobileContants.status_1);
				vipCoupon.setUserId(user.getUserId());
				vipCouponService.update(vipCoupon);
				//更新完毕之后，从缓存中移除此优惠券
				//application.removeAttribute(MobileContants.VIP_COUPON_ID_KEY);
			}
			}
		//钱包余额抵扣后跳转到订单详情页面
		String url="redirect:/order/qbpay.html?orderId="+id;
		return url;

	}
	/**
	 * 当优惠券充足的时候，直接使用优惠券抵扣，不走微信支付
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/order/yhqPay.html", method = RequestMethod.POST)
	public String yhqPay(HttpServletRequest request, HttpServletResponse response, ModelMap model, Car car,String vipCouponId,String lat,String lng,String totalPrice,String projectName){
		Map<String,Object> resultMap=new HashMap<String,Object>();
		boolean flag=false;
		//FmUtils.FmData(request, model);
		String orderId="";
		int id=0;
		int carId=-1;
		String jsParam="";
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if (!StringUtil.isNullOrEmpty(car)){
			car.setUserCarId(user.getUserId());
			//首页判断此车辆是否存在
			Map<String,Object> queryMap=new HashMap<String,Object>();
			queryMap.put("carNo",car.getCarNo());
			Car isExsit=null;
			int count=historyCarService.findAllListCount(queryMap);
			if(count<=0){
				carId= historyCarService.add(car);//新增
				car.setId(carId);
				isExsit=car;
				if(carId>0){
					flag=true;
				}
			}else{//更新
				queryMap.put("userCardId", user.getUserId());
				isExsit.setWashAddr(car.getWashAddr());
				isExsit.setName(car.getName());
				isExsit.setWashInfo(car.getWashInfo());
				isExsit.setMobile(car.getMobile());
				isExsit.setCarColor(car.getCarColor());
				isExsit.setCarSeats(car.getCarSeats());
				isExsit.setCarType(car.getCarType());
				isExsit.setCarNo(car.getCarNo());
				isExsit.setPreTime(car.getPreTime());
				flag=historyCarService.update(isExsit);
				carId=isExsit.getId();
			}
			//**移除录入的车辆信息保存的session**//
			SessionHelper.removeAttribute(request, MobileContants.CAR_SESSION_KEY);
			//*生成订单*//
			Order order=new Order();
			String orderNo=DateUtil.getDateOrderNo();
			order.setOrderId(orderNo);
			String orderTime=DateUtil.getCurrentDateStr();
			order.setCreateTime(orderTime);
			order.setWashTime(car.getPreTime());
			order.setCarNum(car.getCarNo());
			order.setCarColor(car.getCarColor());
			order.setAddress(car.getWashAddr());
			order.setMobile(user.getPhoneNum());
			order.setUserId(user.getUserId());
			order.setUserName(car.getName());
			order.setAddress(car.getWashAddr());
			order.setCarType(car.getCarType());
			order.setDiscription(car.getWashInfo());
			order.setCarId(carId + "");
			order.setPayType(MobileContants.status_4);//优惠券抵扣
			order.setOrderStatus(MobileContants.status_1);//已付款
			order.setLon(lng);
			order.setLat(lat);
			float total_price=0;
			if(!StringUtil.isNullOrEmpty(totalPrice)){
				total_price=Float.parseFloat(totalPrice);
			}
			order.setPrice(total_price);
			order.setWashType(projectName);
			order.setSendOrderStatus(MobileContants.status_0);
			order.setUserId(user.getUserId());
			orderId=OrderUtil.GetOrderNumber("");
			order.setOrderId(orderId);
			id=orderService.add(order);
			//下单完成后保存订单主键到缓存中，修改订单状态的时候要用
			order.setId(id);
			//保存订单完毕后，减去钱包余额
			//user.setAccount(user.getAccount()-total_price);
			//userServices.reduceQianBao(user);
			//同时更新缓存
			SessionHelper.removeAttribute(request,MobileContants.USER_SESSION_KEY);
			SessionHelper.setAttribute(request,MobileContants.USER_SESSION_KEY,user);
			request.getSession().removeAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
			request.getSession().setAttribute(MobileContants.USER_APPLICATION_SESSION_KEY,user);
			//接下来是自动派单
			ServletContext application =request.getSession().getServletContext();
			String autoPaiDan=isAutoPaiDanMethod(request, application, order);
			if(CodeCommon.AUTO_PAIDAN_FLAG.equals(autoPaiDan)){
				//判断完毕，更新订单派单状态
				Order o=new Order();
				o.setId(id);
				o.setSendOrderStatus(MobileContants.status_1);
				orderService.updateStatus(o);
			}
			//减少优惠券
			if(!StringUtil.isNullOrEmpty(vipCouponId)&&!"null".equals(vipCouponId)){
				//下单的时候使用了优惠券抵扣，所以要把此优惠券状态更改为已使用
				long couponId=Long.parseLong(vipCouponId);
				VipCoupon vipCoupon=new VipCoupon();
				vipCoupon.setId(couponId);
				vipCoupon.setStatus(MobileContants.status_1);
				vipCoupon.setUserId(user.getUserId());
				vipCouponService.update(vipCoupon);
				//更新完毕之后，从缓存中移除此优惠券
				application.removeAttribute(MobileContants.VIP_COUPON_ID_KEY);
			}
		}
		//优惠券抵扣后跳转到订单详情页面
		String url="redirect:/order/wxpay.html?orderId="+id;
		return url;

	}
	/**
	 * 微信支付回调方法统一走这里，上面的方式总是出现bug,客户付款成功之后，订单状态不改变
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/order/wxpayHdMethod.html")
	public void wxpayHdMethod(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		System.out.println("我是主动回调的方法，我进来了，终于成功了，原来是因为回调url加登录过滤验证了。。。。。。。");
		ModelAndView mv=new ModelAndView();
		Order order=null;
		Map<String,Object> returnMap=new HashMap<String, Object>();
		Map<String,Object> result=new HashMap<String, Object>();
		boolean flag=false;
		String inputLine;
		String notityXml = "";
		String resXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();

		Map m = parseXmlToList2(notityXml);
		String openid=m.get("openid")+"";
		String return_code=m.get("return_code")+"";//付款成功与否的标志
		String total_fee=m.get("total_fee")+"";//付款金额
		String transaction_id=m.get("transaction_id")+"";//微信支付订单号
		String out_trade_no=m.get("out_trade_no")+"";//商户订单号
		String attach=m.get("attach")+"";//商家数据包
		String time_end=m.get("time_end")+"";//支付时间
			System.out.println("openid==================================="+openid);
			System.out.println("return_code==================================="+return_code);
			System.out.println("transaction_id==================================="+transaction_id);
		Map<String,Object> query=new HashMap<String, Object>();
		query.put("transactionId",transaction_id);
		Order count=orderService.findById(query);
		//从缓存中获取订单
		ServletContext application =request.getSession().getServletContext();
			order= (Order) application.getAttribute(MobileContants.CURRENT_ORDER_KEY);
		String url="";
		if(!StringUtil.isNullOrEmpty(order)){
			url="redirect:/my/order"+order.getId()+".html";
			mv.setViewName(url);
			returnMap.put("page",url);
		}

		if(StringUtil.isNullOrEmpty(count)){
			if(!StringUtil.isNullOrEmpty(order)){
				//更新订单支付状态，同时更新商户订单号和交易单号
				order.setOutTradeNo(out_trade_no);//根据outtradeNo查询订单信息
				returnMap.put("SUCCESS",true);
				if("SUCCESS".equals(return_code)){
					//支付成功
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
					order.setOrderStatus(MobileContants.status_1);//已支付
					order.setTransactionId(transaction_id);
					//付款成功之后将当前订单缓存key清楚
					SessionHelper.removeAttribute(request,MobileContants.CURRENT_ORDER_KEY);
				}else{
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
				}
				//根据order主键更新订单信息
				System.out.println("更新订单前order=================="+order.getId());
				flag=orderService.updateOrderByOutTradeNo(order);
				//减少优惠券,先判断下单的时候是否使用了优惠券，如果使用了，那么需要更新一下优惠券使用状态
				Users user= (Users) application.getAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
				String vipcouponId=order.getVipCouponId();
				if(!StringUtil.isNullOrEmpty(vipcouponId)){
					//下单的时候使用了优惠券抵扣，所以要把此优惠券状态更改为已使用
					long couponId=Long.parseLong(vipcouponId);
					VipCoupon vipCoupon=new VipCoupon();
					vipCoupon.setId(couponId);
					vipCoupon.setStatus(MobileContants.status_1);
					vipCoupon.setUserId(user.getUserId());
					vipCouponService.update(vipCoupon);
					//更新完毕之后，从缓存中移除此优惠券
					//application.removeAttribute(MobileContants.VIP_COUPON_ID_KEY);
				}
				//判断下单的时候是否使用了余额抵扣，如果使用了余额抵扣，那么付款成功之后，还需要把会员里面的余额做相应的减少操作
				float qianbao=order.getQianbao();
				if(qianbao>0){//说明使用了钱包抵扣，那么做递减操作
					user.setAccount(user.getAccount()-qianbao);
					userServices.reduceQianBao(user);
				}
				//更新订单成功之后，重新更新一下缓存
				application.removeAttribute(MobileContants.CURRENT_ORDER_KEY);
				application.setAttribute(MobileContants.CURRENT_ORDER_KEY,order);
			}
			//自动派单
			String autoPaiDan =isAutoPaiDanMethod(request, application, order);
		}
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("success",flag);
		returnMap.put("success", flag);
		returnMap.put("orderId",order.getId());
		//return mv;
	}
	@RequestMapping(value = "/order/wxpayUpdate.html")
	@ResponseBody
	public ModelAndView wxpayUpdate(HttpServletRequest request, HttpServletResponse response,String id) {
		System.out.println("weixin pay >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		ModelAndView mv=new ModelAndView();
		try{
			ServletContext application =request.getSession().getServletContext();
			Order order= (Order) application.getAttribute(MobileContants.CURRENT_ORDER_KEY);
			String url="";
			if(!StringUtil.isNullOrEmpty(order)){
				url="redirect:/my/order"+id+".html";
				mv.setViewName(url);
			}
			System.out.println("魂村中获取order=================================="+order);
			//if(!StringUtil.isNullOrEmpty(order)){
			//更新订单支付状态，同时更新商户订单号和交易单号
			//order.setOutTradeNo(out_trade_no);//根据outtradeNo查询订单信息
			//returnMap.put("SUCCESS",true);
			//if("SUCCESS".equals(return_code)){
			Order updateOrder=new Order();
			updateOrder.setId(Integer.parseInt(id));
			updateOrder.setOrderStatus(MobileContants.status_1);//已支付
			//order.setTransactionId(transaction_id);
			//付款成功之后将当前订单缓存key清楚
			SessionHelper.removeAttribute(request,MobileContants.CURRENT_ORDER_KEY);
			//}
			//根据order主键更新订单信息
			//System.out.println("更新订单前order=================="+order.getId());
			boolean flg=orderService.updateOrderByOutTradeNo(updateOrder);
			System.out.println("更新结果====================="+flg);
			//减少优惠券,先判断下单的时候是否使用了优惠券，如果使用了，那么需要更新一下优惠券使用状态
			Users user= (Users) application.getAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
			System.out.println("order==========================================="+order);
			String vipcouponId=order.getVipCouponId();
			System.out.println("vipcouponId================================================"+vipcouponId);
			if(!StringUtil.isNullOrEmpty(vipcouponId)){
				//下单的时候使用了优惠券抵扣，所以要把此优惠券状态更改为已使用
				long couponId=Long.parseLong(vipcouponId);
				VipCoupon vipCoupon=new VipCoupon();
				vipCoupon.setId(couponId);
				vipCoupon.setStatus(MobileContants.status_1);
				vipCoupon.setUserId(user.getUserId());
				vipCouponService.update(vipCoupon);
				//更新完毕之后，从缓存中移除此优惠券
				//application.removeAttribute(MobileContants.VIP_COUPON_ID_KEY);
			}
			//判断下单的时候是否使用了余额抵扣，如果使用了余额抵扣，那么付款成功之后，还需要把会员里面的余额做相应的减少操作
			float qianbao=order.getQianbao();
			System.out.println("qianbao=============================================="+qianbao);
			if(qianbao>0){//说明使用了钱包抵扣，那么做递减操作
				user.setAccount(user.getAccount()-qianbao);
				userServices.reduceQianBao(user);
			}
			//自动派单
			String autoQiangDan=coreService.getValue(CodeCommon.AUTO_QAINGDAN);
			if(CodeCommon.AUTO_PAIDAN_FLAG.equals(autoQiangDan)){
				url="redirect:/my/order"+id+".html";
				mv.setViewName(url);
				order.setOrderStatus(updateOrder.getOrderStatus());
				String autoPaiDan =isAutoPaiDanMethod(request, application, order);
				System.out.println("autoPaiDan======================================================="+autoPaiDan);
			}else {
				String qingdanTime=application.getAttribute(MobileContants.QIANGDAN_TIME_KEY)+"";
				isAutoQiangDanMethod(request, application, order);
				url = "redirect:/qiangdan/index.html";
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		return mv;
	}
	/**
	 * 自动派单方法
	 * @param request
	 * @param application
	 * @param order
	 */
	private String isAutoPaiDanMethod(HttpServletRequest request, ServletContext application, Order order) {
		String url;//下完单后是否开启自动派单功能
		String autoPaiDan=coreService.getValue(CodeCommon.AUTO_PAIDAN);
		if(CodeCommon.AUTO_PAIDAN_FLAG.equals(autoPaiDan)){
			//根据经纬度派单给最近的洗车工师父
			SendOrder sendOrder=new SendOrder();
			//根据经纬度获取最近的洗车工师父
			AdminBo bo=userServices.findAdminByLatLng(order.getLat(), order.getLon());
			sendOrder.setCarNo(order.getCarNum());
			sendOrder.setOrderId(order.getId() + "");
			sendOrder.setName(order.getCarType());
			sendOrder.setPreTime(order.getWashTime());
			sendOrder.setMobile(order.getMobile());
			String  currentTime=com.zero2ipo.framework.util.DateUtil.getCurrentTime();
			sendOrder.setSendOrderTime(currentTime);
			sendOrder.setUserId(bo.getUserId());
			Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
			if(!StringUtil.isNullOrEmpty(user)){
				sendOrder.setOperatorId(user.getUserId());
			}
			sendOrder.setStatus(MobileContants.SEND_ORDER_STATUS_1);

			//派单完成后是否给管理员发送短信或者微信
			int returnCode=0;//模板消息返回码
			String returnMsg;//模板消息返回信息
			String isSendMessage=coreService.getValue(CodeCommon.IS_SENDMESSAGE_TO_ADMIN);
			if(CodeCommon.IS_SENDMESSAGE_TO_ADMIN_FLAG.equals(isSendMessage)){//开启给管理发送派单短信通知
				String sendMessageFlag=coreService.getValue(CodeCommon.SEND_MESSAGE_FLAG);
				if(sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_DUANXIN)){
					//发送短信通知
					if(!StringUtil.isNullOrEmpty(order)){
						String sendDuanXinToAdmin=coreService.getValue(CodeCommon.SEND_DUANXIN_TO_ADMIN);
						SendMessageVCode(request,order.getMobile(),"您有一条新的派单通知，车主手机号码:"+order.getMobile()+" 车牌号:"+order.getCarNum()+" 洗车类型:"+order.getCarType(),sendDuanXinToAdmin);
					}
				}
				if(sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_WEIXIN)){
					//发送微信通知
					String openId=bo.getIp();//获取洗车工绑定的微信openid
					String templateMessageId=application.getAttribute(MobileContants.PAIDAN_TEMPLATE_KEY)+"";
					if(StringUtil.isNullOrEmpty(templateMessageId)){
						templateMessageId=coreService.getValue(CodeCommon.PAIDAN_TEMPLATE_MESSAGE);
					}
					templateMessageId=templateMessageId.trim();
					String washType=order.getWashType();
					//查询域名
					String  domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";//首先从缓存中获取
					if(StringUtil.isNullOrEmpty(domain)){//
						domain=coreService.getValue(CodeCommon.DOMAIN);
					}
					url=domain+"/renwu/order"+order.getId()+".html";

					WxTemplate wxTemplate= TemplateMessageUtils.getPaiDanTemplate(openId, templateMessageId, url, order, bo);
					//发送模板消息
					Object appId=application.getAttribute(MobileContants.APPID_KEY);
					if(StringUtil.isNullOrEmpty(appId)){
						appId=coreService.getValue(CodeCommon.APPID);
					}
					Object appsecret=application.getAttribute(MobileContants.APPSCRET_KEY);
					if(StringUtil.isNullOrEmpty(appsecret)){
						appsecret=coreService.getValue(CodeCommon.APPSECRET);
					}
					returnMsg="派单参数:appid="+appId+",appsecret="+appsecret+",openId="+openId+",templateMessageId="+templateMessageId+",toUser="+wxTemplate.getTouser();
					returnCode= coreService.send_template_message(appId + "", appsecret + "", openId, wxTemplate);
					//保存派单记录到数据库中
					sendOrder.setReturnCode(returnCode);
					sendOrder.setReturnMsg(returnMsg);
				}
			}

			sendOrderService.addSendOrder(sendOrder);
		}
		return autoPaiDan;
	}

	/**
	 * 倒计时结束的时候，还没有人接单的话,那么自动派给距离最近的人
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qiangdan/timeover.html", method = RequestMethod.POST)
	@ResponseBody
	public void isAutoPaiDanMethod(HttpServletRequest request,String id) {
		//首先根据id判断是否存在派单信息
		SendOrder  sendOrder=sendOrderService.findSendOrderByOrderId(id);
		if(StringUtil.isNullOrEmpty(sendOrder)){
			//开启自动派单
			ServletContext application =request.getSession().getServletContext();
			Order order= (Order) application.getAttribute(MobileContants.CURRENT_ORDER_KEY);
			isAutoPaiDanMethod(request,application,order);
		}
	}
	/**
	 * qiang dan success
	 * @return
	 */
	@RequestMapping(value = "/qiandan/test.html", method = RequestMethod.GET)
	public ModelAndView qiandanTest(HttpServletRequest request,
									HttpServletResponse response, ModelMap model,String orderId) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		String url=MobilePageContants.QIANG_DAN_SUCCESS_PAGE;
		mv.setViewName(url);
		mv.addObject("orderId",orderId);
		ServletContext application=request.getSession().getServletContext();
		Map<String,Object> queryMap=new HashMap<String, Object>();
		queryMap.put("id",orderId);
		Order order=orderService.findById(queryMap);
		application.setAttribute(MobileContants.CURRENT_ORDER_KEY, order);
		isAutoQiangDanMethod(request, application, order);
		return mv;
	}

	/**
	 * 抢单方法
	 * @param application
	 * @param order
	 */
	private void isAutoQiangDanMethod(HttpServletRequest request2, ServletContext application, Order order) {
		String url;//下完单后是否开启自动派单功能
		//派单完成后是否给管理员发送短信或者微信
		int returnCode=0;//模板消息返回码
		String returnMsg;//模板消息返回信息
		String isSendMessage=coreService.getValue(CodeCommon.IS_SENDMESSAGE_TO_ADMIN);
		if(CodeCommon.IS_SENDMESSAGE_TO_ADMIN_FLAG.equals(isSendMessage)){//开启给管理发送派单短信通知
			String sendMessageFlag=coreService.getValue(CodeCommon.SEND_MESSAGE_FLAG);
			if(CodeCommon.SEND_MESSAGE_WEIXIN.equals(sendMessageFlag)){
				//查询所有的洗车工
				List<AdminBo> adminList=userServices.findAdminList();
				for(int i=0;i<adminList.size();i++){
					AdminBo bo=adminList.get(i);
					//发送微信通知
					String openId=bo.getIp();//获取洗车工绑定的微信openid
					String templateMessageId=application.getAttribute(MobileContants.QIANGDAN_TEMPLATE_KEY)+"";
					if(StringUtil.isNullOrEmpty(templateMessageId)){
						templateMessageId=coreService.getValue(CodeCommon.QIANGDAN_TEMPLATE_MESSAGE);
					}
					templateMessageId=templateMessageId.trim();
					String washType=order.getWashType();
					//查询域名
					String  domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";//首先从缓存中获取
					if(StringUtil.isNullOrEmpty(domain)){//
						domain=coreService.getValue(CodeCommon.DOMAIN);
					}
					url=domain+"/qiangdan/index.html?id="+order.getId();
					String qingdanTime=application.getAttribute(MobileContants.QIANGDAN_TIME_KEY)+"";
					WxTemplate wxTemplate= TemplateMessageUtils.getQiangDanTemplate(openId, templateMessageId, url, order, bo,qingdanTime);
					//发送模板消息
					Object appId=application.getAttribute(MobileContants.APPID_KEY);
					if(StringUtil.isNullOrEmpty(appId)){
						appId=coreService.getValue(CodeCommon.APPID);
					}
					Object appsecret=application.getAttribute(MobileContants.APPSCRET_KEY);
					if(StringUtil.isNullOrEmpty(appsecret)){
						appsecret=coreService.getValue(CodeCommon.APPSECRET);
					}
					returnMsg="派单参数:appid="+appId+",appsecret="+appsecret+",openId="+openId+",templateMessageId="+templateMessageId+",toUser="+wxTemplate.getTouser();
					returnCode= coreService.send_template_message(appId + "", appsecret + "", openId, wxTemplate);
				}
			}
			if(sendMessageFlag.contains(CodeCommon.SEND_MESSAGE_DUANXIN)) {
				String sendDuanXinToAdmin=coreService.getValue(CodeCommon.SEND_DUANXIN_TO_ADMIN);
				SendMessageVCode(request2,order.getMobile(),"手机号:"+order.getMobile()+"车牌号:"+order.getCarNum()+",洗车类型:"+order.getCarType(),sendDuanXinToAdmin);
			}
		}
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
	 * description: 解析微信通知xml
	 *
	 * @param xml
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	private static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	//获取微信支付参数信息
	public String getWXJsParamForNative(HttpServletRequest request, float total_free) {
		Map<String, Object> result = new HashMap<String, Object>();
		MdlPay pay = new MdlPay();
		WXPrepay prePay = getWxPrepay(request);
		float b = (float) (Math.round(total_free * 100)) / 100;
		prePay.setTotal_fee((int) (b * 100) + "");
		prePay.setTrade_type("JSAPI");
		String jsParam = "";
		//此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		if (!"".equals(prepayid) && prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(prePay.getAppid(), prePay.getPartnerKey(), prepayid);
		}
		System.out.println("jsParam=======================================================================" + jsParam);
		return jsParam;
	}
	/**
	 * 动态获取wxPrepay
	 */
	private WXPrepay getWxPrepay(HttpServletRequest request) {
		ServletContext application =request.getSession().getServletContext();
		String partnerId =application.getAttribute(MobileContants.PARTNERID_KEY)+"";
		String appid =application.getAttribute(MobileContants.APPID_KEY)+"";
		String partnerValue = application.getAttribute(MobileContants.PARTNERVALUE_KEY)+"";
		String spbill_create_ip = request.getRemoteAddr();
		WXPrepay prePay = new WXPrepay();
		String prePayBody = application.getAttribute(MobileContants.PREPAYBODY_KEY)+"";
		String domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";
		prePay.setAppid(appid);
		prePay.setBody(prePayBody);
		prePay.setPartnerKey(partnerValue);
		prePay.setMch_id(partnerId);
		System.out.println("weixin pay 回调地址================================================================="+domain+notifyUrl);
		prePay.setNotify_url(domain+notifyUrl);
		String outTradeNo=UUID.randomUUID().toString().replace("-","");
		prePay.setOut_trade_no(outTradeNo);//每次重新生成交易单号，防止订单重复，但是需要把订单里面的outTradeNo也修改了
		prePay.setSpbill_create_ip(spbill_create_ip);
		String openid = "";
		//首先从当前登录的账号信息中获取openid
		Object o=  SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(o instanceof Users){
			Users u= (Users) o;
			if(!StringUtil.isNullOrEmpty(u)){
				openid=u.getOpenId();//如果数据库中不存在openid，再从缓存中读取
				if(StringUtil.isNullOrEmpty(openid)){
					openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);//从缓存中获取，这里有时会获取不到，所有要从数据库中读取
				}
			}
		}
		prePay.setOpenid(openid);
		return prePay;
	}

	/**
	 * 获取微信支付参数
	 * appId 微信appid
	 * partnerId 微信支付商户号
	 * partnerkey 微信支付商户秘钥
	 *
	 * @return
	 */
	public MdlPay getMdlPay(HttpServletRequest request) {
		MdlPay pay = new MdlPay();
		ServletContext application =request.getSession().getServletContext();
		String partnerId =application.getAttribute(MobileContants.PARTNERID_KEY)+"";
		String appid =application.getAttribute(MobileContants.APPID_KEY)+"";
		String partnerValue = application.getAttribute(MobileContants.PARTNERVALUE_KEY)+"";
		pay.setAppId(appid);
		pay.setPartnerId(partnerId);
		pay.setPartnerKey(partnerValue);
		return pay;
	}

	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService ;
	@Resource(name = "addressService")
	private IAddressService addressService;
	@Resource(name = "orderService")
	private IOrderService orderService;
	@Resource(name = "washCouponService")
	private IWashCouponService washCouponService;
	@Resource(name = "vipCouponService")
	private IVipCouponService vipCouponService;
	@Resource(name = "coreService")
	private ICoreService coreService;
	@Resource(name = "sendOrderService")
	private ISendOrderService sendOrderService;
	@Resource(name = "userServices")
	private IUserServices userServices ;
}

