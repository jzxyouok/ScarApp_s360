package com.zero2ipo.common.freemarker.directives;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.freemarker.DirectiveUtils;
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
public class HistoryCarDirective implements TemplateDirectiveModel{
	private static final String PARAM_OPENID = "openid";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Car> list=null;
		String openId= DirectiveUtils.getString(PARAM_OPENID, params);
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(openId)){
				queryMap.put("openId",openId);
				list=historyCarService.findAllList(queryMap);
			}else{
				list=new ArrayList<Car>();
			}
			env.setVariable("historyCars", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
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
