package com.zero2ipo.common.freemarker.directives;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * 我的订单标签
 * @author zhengYunfei
 *
 */
public class FindSendOrderByIorderIdDirective implements TemplateDirectiveModel{
	private static final String ID="id";
	private static final String ORDER_ID="orderId";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		SendOrder order=null;
		try {
			String id=DirectiveUtils.getString(ID, params);
			String orderId=DirectiveUtils.getString(ORDER_ID, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(id)){
				queryMap.put("id", id);
			}
			if(!StringUtil.isNullOrEmpty(orderId)){
				queryMap.put("orderId", orderId);
			}
			order=orderService.findSendOrderByOrderId(queryMap);
			if(!StringUtil.isNullOrEmpty(order)){
				env.setVariable("sendOrder", ObjectWrapper.DEFAULT_WRAPPER.wrap(order));
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
