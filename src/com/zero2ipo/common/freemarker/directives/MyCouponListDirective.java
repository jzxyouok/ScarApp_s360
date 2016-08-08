package com.zero2ipo.common.freemarker.directives;

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
 * 我的洗车券标签
 * @author zhengYunfei
 *
 */
public class MyCouponListDirective implements TemplateDirectiveModel{
	private static final String OPEN_ID="openid";
	private static final String MOBILE="mobile";
	private static final String CARNO="carNo";
	/*通过车牌号限制是否是首单的标志*/
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<UserCoupon> list=null;
		int flg=1;//默认是
		try {
			String openId=DirectiveUtils.getString(OPEN_ID, params);
			String mobile=DirectiveUtils.getString(MOBILE, params);
			String carNo=DirectiveUtils.getString(CARNO, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(openId)){
				queryMap.put("userId", openId);
			}
			if(!StringUtil.isNullOrEmpty(mobile)){
				queryMap.put("mobile", mobile);
			}
			queryMap.put("carNo", carNo);
			list=washCouponService.IsSd(queryMap);
			//Map<String,Object> m=new HashMap<String, Object>();
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				flg=0;
				env.setVariable("couponList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}
			env.setVariable("isShouDan", ObjectWrapper.DEFAULT_WRAPPER.wrap(flg));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("flg================"+flg);
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "washCouponService")
	private IWashCouponService washCouponService;
}
