package com.zero2ipo.pay.web;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IAddressService;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderController {
	@Resource(name = "orderService")
	private IOrderService orderService ;
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService;
	@Resource(name = "addressService")
	private IAddressService addressService;
	//添加常用地址
	@RequestMapping(value = "/order/addloveaddress", method = RequestMethod.GET)
	public ModelAndView preCreateOrder(HttpServletRequest request, HttpServletResponse response, Model model,ModelMap map,String  cid) {
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.ADD_ADDRESS);
		mv.addObject("cid", cid);
		return mv;
	}
	@RequestMapping(value = "/order/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> createOrder(HttpServletRequest request, HttpServletResponse response, Model model,ModelMap map,Order order) {
		Map<String,Object> result=new HashMap<String, Object>();
		int orderId=orderService.add(order);
		result.put("orderId", orderId);
		return result;
	}
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	/**
	 * 根据订单id查询订单详情页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/pay/orderdetail/{id}.html", method = RequestMethod.GET)
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
	
}
