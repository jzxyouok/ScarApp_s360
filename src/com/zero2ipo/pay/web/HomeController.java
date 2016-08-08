package com.zero2ipo.pay.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.web.SessionHelper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService ;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/wxpay", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> home(HttpServletRequest request, HttpServletResponse response,Locale locale, Model model,ModelMap map) {
		FmUtils.FmData(request, map);
		List<Car> list=null;
		Map<String,Object> returnMap=new HashMap<String, Object>();
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			String openId= SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);
			if(!StringUtil.isNullOrEmpty(openId)){
				queryMap.put("openId",openId);
				list=historyCarService.findAllList(queryMap);
			}else{
				openId="oR_pAwi2AC7WYfUzyrb5QaFPqcFU";
				queryMap.put("openId",openId);
				list=historyCarService.findAllList(queryMap);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		returnMap.put("historyList", list);
		System.out.println(list.size());
		return returnMap;
	}
	
}
