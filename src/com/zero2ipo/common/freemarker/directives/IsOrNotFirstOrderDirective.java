package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
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
public class IsOrNotFirstOrderDirective implements TemplateDirectiveModel{
	private static final String CAR_NO="carNo";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		int flg=0;//表示是首单
		try {
			String carNo=DirectiveUtils.getString(CAR_NO, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(carNo)){
				queryMap.put("carNo", carNo);
			}
			flg=orderService.findIsOrNotFirstOrder(queryMap);
		   
			env.setVariable("isShouDan", ObjectWrapper.DEFAULT_WRAPPER.wrap(flg));
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
