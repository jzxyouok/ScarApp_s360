package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.UserCoupon;
import com.zero2ipo.common.freemarker.DirectiveUtils;
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
public class WashCarCouponListDirective implements TemplateDirectiveModel{
	private static final String MOBILE="mobile";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<UserCoupon> list=null;
		try {
			String mobile=DirectiveUtils.getString(MOBILE, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(mobile)){
				queryMap.put("mobile", mobile);
			}
			list=washCouponService.findUserCouponList(queryMap);
			Map<String,Object> m=new HashMap<String, Object>();
			
			for(int i=0;i<list.size();i++){
				String couponId=list.get(i).getCouponId();
				m.put("id", couponId);
				GgwashCoupon ggwashCoupon=washCouponService.findById(m);
				if(!StringUtil.isNullOrEmpty(ggwashCoupon)){
					list.get(i).setName(ggwashCoupon.getName());
					list.get(i).setPic(ggwashCoupon.getPic());
				}
			}
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
