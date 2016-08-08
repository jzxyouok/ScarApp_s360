package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import com.zero2ipo.mobile.services.bsb.ISendOrderService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 我的订单标签
 * @author zhengYunfei
 *
 */
public class FindOrderByIdDirective implements TemplateDirectiveModel{
	private static final String ID="id";
	public void execute(Environment env, Map params, TemplateModel[] model,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		Order order=null;
		try {
			String id=DirectiveUtils.getString(ID, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			queryMap.put("id", id);
			order=orderService.findById(queryMap);
			if(!StringUtil.isNullOrEmpty(order)){
				order.setStatus(order.getOrderStatus());
				//根据订单id查询派单信息
				SendOrder sendOrder=sendOrderService.findSendOrderByOrderId(id);
				if(!StringUtil.isNullOrEmpty(sendOrder)){
					order.setSendOrderStatus(sendOrder.getStatus());
				}
				env.setVariable("order", ObjectWrapper.DEFAULT_WRAPPER.wrap(order));
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
	@Resource(name = "sendOrderService")
	private ISendOrderService sendOrderService;
}
