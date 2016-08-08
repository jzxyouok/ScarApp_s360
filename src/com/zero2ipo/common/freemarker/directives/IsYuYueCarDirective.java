package com.zero2ipo.common.freemarker.directives;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * 历史车辆标签
 * @author zhengYunfei
 *
 */
public class IsYuYueCarDirective implements TemplateDirectiveModel{
	private static final String PRE_DATE="date";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Car car=null;
		String date=DirectiveUtils.getString(PRE_DATE,params);
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			queryMap.put("preTime", date);
			queryMap.put("status", MobileContants.status_1);
			if(!StringUtil.isNullOrEmpty(date)){
				car=historyCarService.findById(queryMap);
				if(!StringUtil.isNullOrEmpty(car)){
					env.setVariable("precar", ObjectWrapper.DEFAULT_WRAPPER.wrap(car));
					System.out.println(car.getCarNo());
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "historyCarService")
	private IHistoryCarService historyCarService;
}
