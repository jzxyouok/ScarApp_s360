package com.zero2ipo.mobile.action;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.bsb.ISendOrderService;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.weixin.services.message.ICoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 手机端主页调整控制
 * @author zhengyunfei
 * @date 2015-04-22
 *
 */

@Controller
public class QiangDanPageAct {
	/**
	 * qiang dan
	 * @return
	 */
	@RequestMapping(value = "/qiangdan/index.html", method = RequestMethod.GET)
	public ModelAndView qiangdan(HttpServletRequest request,
								 HttpServletResponse response, ModelMap model,String id) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		String url=MobilePageContants.QIANG_DAN_PAGE;
		String qiangdanTime=request.getSession().getServletContext().getAttribute(MobileContants.QIANGDAN_TIME_KEY)+"";
		AdminBo adminBo= (AdminBo) SessionHelper.getAttribute(request, MobileContants.ADMIN_SESSION_KEY);
		if(StringUtil.isNullOrEmpty(adminBo)){
			url=MobilePageContants.ADMIN_LOGIN_PAGE;
		}else{
			//首先根据orderId判断此订单是否已经派单
			SendOrder  sendOrder=sendOrderService.findSendOrderByOrderId(id);
			if(!StringUtil.isNullOrEmpty(sendOrder)){
				//已来晚了，此单已被抢,还要判断是否是自己抢过的订单，如果是，那么直接跳转到任务页面
				if(sendOrder.getUserId().equals(adminBo.getUserId())){
					//url=MobilePageContants.QIANG_DAN_LATER_PAGE;
					//跳转到任务页面
					url=MobilePageContants.ADMIN_ORDER_DETAIL_PAGE;
					mv.addObject("orderId", id);
				}else{
					url=MobilePageContants.QIANG_DAN_LATER_PAGE;
					mv.addObject("sendOrder",sendOrder);
				}

			}else{
				Map<String,Object> queryMap=new HashMap<String, Object>();
				queryMap.put("id",id);
				Order order=orderService.findById(queryMap);
			}
		}
		mv.setViewName(url);
		mv.addObject("id",id);
		mv.addObject("qiangdanTime",qiangdanTime);
		return mv;
	}
	/**
	 * qiang dan success
	 * @return
	 */
	@RequestMapping(value = "/qiangdan/success.html", method = RequestMethod.GET)
	public ModelAndView qiangdan0fSuccess(HttpServletRequest request,
										  HttpServletResponse response, ModelMap model,String orderId) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView();
		String url=MobilePageContants.QIANG_DAN_SUCCESS_PAGE;
		mv.setViewName(url);
		mv.addObject("orderId",orderId);
		return mv;
	}

	@RequestMapping(value = "/qiangdan/save.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> qiangdanAjax(HttpServletRequest request,
										   HttpServletResponse response, ModelMap model,String id) {
		Map<String,Object> returnMap=new HashMap<String, Object>();
		AdminBo adminBo= (AdminBo) SessionHelper.getAttribute(request, MobileContants.ADMIN_SESSION_KEY);
		//从缓存中获取order
		ServletContext application =request.getSession().getServletContext();
		Map<String,Object> queryMap=new HashMap<String, Object>();
		queryMap.put("id",id);
		Order order=orderService.findById(queryMap);
		SendOrder sendOrder=new SendOrder();
		sendOrder.setCarNo(order.getCarNum());
		sendOrder.setOrderId(order.getId() + "");
		sendOrder.setName(order.getCarType());
		sendOrder.setPreTime(order.getWashTime());
		sendOrder.setMobile(order.getMobile());
		String  currentTime=com.zero2ipo.framework.util.DateUtil.getCurrentTime();
		sendOrder.setSendOrderTime(currentTime);
		sendOrder.setUserId(adminBo.getUserId());
		Users user=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(user)){
			sendOrder.setOperatorId(user.getUserId());
		}
		sendOrder.setStatus(MobileContants.SEND_ORDER_STATUS_1);
		sendOrderService.addSendOrder(sendOrder);
		//抢单成功,发送抢单通知

		returnMap.put("success",true);
		return returnMap;
	}

	@Resource(name = "coreService")
	protected ICoreService coreService;
	@Resource(name = "userServices")
	private IUserServices userServices ;
	@Resource(name = "sendOrderService")
	private ISendOrderService sendOrderService;
	@Resource(name = "orderService")
	private IOrderService orderService;
}
