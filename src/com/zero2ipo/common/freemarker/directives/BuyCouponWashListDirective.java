package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IWashCouponService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 洗车券标签
 * @author zhengYunfei
 *
 */
public class BuyCouponWashListDirective implements TemplateDirectiveModel{
	
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<GgwashCoupon> list=null;
		try {
			Map<String,Object> queryMap=new HashMap<String, Object>();
			list=washCouponService.findAllList(queryMap);
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				env.setVariable("couponList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "washCouponService")
	private IWashCouponService washCouponService;
}
