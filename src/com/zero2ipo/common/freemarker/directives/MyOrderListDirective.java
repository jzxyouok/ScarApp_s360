package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 我的订单标签
 * @author zhengYunfei
 *
 */
public class MyOrderListDirective implements TemplateDirectiveModel{
	private static final String ORDER_ID="orderId";
	private static final String MOBILE="mobile";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Order> list=null;
		try {
			String orderId=DirectiveUtils.getString(ORDER_ID, params);
			String mobile=DirectiveUtils.getString(MOBILE, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			//if(StringUtil.isNullOrEmpty(openId)){
				//openId=MobileUserContants.DEFAULT_OPEN_ID;
			//}
			if(!StringUtil.isNullOrEmpty(orderId)){
				
				queryMap.put("id", orderId);
			}
			if(!StringUtil.isNullOrEmpty(mobile)){
							
			     queryMap.put("mobile", mobile);
			}
			list=orderService.findAllList(queryMap);
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				env.setVariable("myOrderList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "orderService")
	private IOrderService orderService;
}
