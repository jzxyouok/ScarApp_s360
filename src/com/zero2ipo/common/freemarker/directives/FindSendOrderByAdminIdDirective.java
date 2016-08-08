package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.bsb.ISendOrderService;
import com.zero2ipo.mobile.services.user.IUserServices;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 根据洗车工id查询洗车工接单信息
 * @author zhengYunfei
 *
 */
public class FindSendOrderByAdminIdDirective implements TemplateDirectiveModel{
	private static final String USER_ID="userId";
	private static final String STATUS="status";
	private static final String START_DATE="startDate";
	private static final String END_DATE="endDate";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<SendOrder> list=null;
		try {
			String userId=DirectiveUtils.getString(USER_ID, params);
			String status=DirectiveUtils.getString(STATUS, params);
			String startDate=DirectiveUtils.getString(START_DATE, params);
			String endDate=DirectiveUtils.getString(END_DATE, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			Map<String,Object> orderMap=new HashMap<String,Object>();
			Map<String,Object> carMap=new HashMap<String,Object>();
			queryMap.put("userId", userId);
			queryMap.put("status", status);
			if(!StringUtil.isNullOrEmpty(startDate)){
				queryMap.put("startDate",startDate);
			}
			if(!StringUtil.isNullOrEmpty(endDate)){
				queryMap.put("endDate",endDate);
			}
			list=sendOrderService.findSendOrderInfoList(queryMap);
			int count=list.size();
			if(!StringUtil.isNullOrEmpty(list)){
				for(int i=0;i<list.size();i++) {
					String orderId = list.get(i).getOrderId();
					orderMap.put("id", orderId);
					Order order = orderService.findById(orderMap);
					if (!StringUtil.isNullOrEmpty(order)) {
						list.get(i).setMobile(order.getMobile());
						list.get(i).setCarNo(order.getCarNum());
						list.get(i).setPreTime(order.getWashTime());
						list.get(i).setMoney(order.getPrice()+"");
						list.get(i).setAddress(order.getAddress());
						String carId = order.getCarId();
						carMap.put("id",carId);
						Car car=historyCarService.findById(carMap);
						if(!StringUtil.isNullOrEmpty(car)){
							list.get(i).setName(car.getName());
						}

					}
				}
				env.setVariable("sendOrderList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "sendOrderService")
	private ISendOrderService sendOrderService;
	@Resource(name = "orderService")
	private IOrderService orderService;
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService;
	@Resource(name = "userServices")
	private IUserServices userServices;
}
