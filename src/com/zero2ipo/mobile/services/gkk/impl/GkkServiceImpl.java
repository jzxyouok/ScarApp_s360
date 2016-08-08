package com.zero2ipo.mobile.services.gkk.impl;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.dictionary.IDictionaryService;
import com.zero2ipo.mobile.services.gkk.IGkkService;
import com.zero2ipo.mobile.utils.DateUtil;
import com.zero2ipo.weixin.services.message.ICoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基金服务层接口实现类
 * @author zhengyunfei
 *
 */
@Component("gkkService")
public class GkkServiceImpl implements IGkkService {
/*
* 模版服务接口注入
*/
	@Autowired
	protected IHistoryCarService historyCarService ;
	@Autowired
	protected ICoreService coreService ;

	/*
 * 文章服务接口注入
 */

	/*
     * 字典表服务接口注入
     */
	@Autowired
	protected IDictionaryService dictionaryService ;
	public ModelMap initGkkPage(ModelMap model){
		List<Car> list=new ArrayList<Car>();
		ModelAndView mv=new ModelAndView();
		String openId="";
		mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(StringUtil.isNullOrEmpty(openId)){
				openId="1";
			}
			queryMap.put("openId",openId);
			list=historyCarService.findAllList(queryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("carList",list);
		model.put("carList", list);
		return model;
	}

	/**
	 * 公开课初始化
	 * @param model
	 * @return
	 */
	public ModelMap initGkkPage(ModelMap model,String openid,Users user){
		List<Car> list=new ArrayList<Car>();
		Car car=null;
		ModelAndView mv=new ModelAndView();
		mv.setViewName(MobilePageContants.FM_PAGE_MAIN);
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(user)){
				queryMap.put("mobile",user.getPhoneNum());
				queryMap.put("userId",user.getUserId());
				list=historyCarService.findAllList(queryMap);
			}
			 String days=coreService.getValue(CodeCommon.PRE_TIME_DAYS);
			 String hours=coreService.getValue(CodeCommon.PRE_TIME_HOURS);
			 List<String> preDates=DateUtil.getLast2Hours(Integer.parseInt(days),Integer.parseInt(hours));
			 model.put("preDates", preDates);


		} catch (Exception e) {
			e.printStackTrace();
		}
		mv.addObject("carList",list);
		System.out.println("查询出来的车轮======"+list.size());
		if(list.size()>0){
			car=list.get(0);
		}else{
			car =new Car();
			car.setMobile(user.getPhoneNum());
		}
		model.put("bo",car);
		mv.addObject("bo", "car");
		model.put("user", user);
		return model;
	}
	/**
	 * 个人中心 我的订单
	 * @param model
	 * @return
	 */
	public ModelMap initMyOrder(ModelMap model,String openId,String page,Users user){
		ModelAndView mv=new ModelAndView();
		List<Car> list=new ArrayList<Car>();
		Car car=null;
		mv.setViewName(page);
		model.put("openid", openId);
		mv.addObject("openid", openId);
		if(!StringUtil.isNullOrEmpty(user)){
			try {
				Map<String,Object> queryMap=new HashMap<String,Object>();
				if(!StringUtil.isNullOrEmpty(user)){
					queryMap.put("mobile",user.getPhoneNum());
					queryMap.put("userId",user.getUserId());
					list=historyCarService.findAllList(queryMap);
				}
				 List<String> preDates=DateUtil.getLast2Hours();
				 model.put("preDates", preDates);

			} catch (Exception e) {
				e.printStackTrace();
			}
			mv.addObject("carList",list);
			System.out.println("查询出来的车轮======"+list.size());
			if(list.size()>0){
				car=list.get(0);
				//car.setCarNo(user.getAccount());
			}else{
				car =new Car();
				car.setMobile(user.getPhoneNum());
			}
			model.put("bo",car);
			mv.addObject("bo", "car");
			model.put("user", user);
			model.put("mobile", user.getPhoneNum());
		}else{
			mv.setViewName(MobilePageContants.FM_USER_LOGIN);
		}
		return model;
	}
}
