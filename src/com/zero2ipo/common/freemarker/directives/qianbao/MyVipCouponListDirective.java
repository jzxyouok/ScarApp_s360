package com.zero2ipo.common.freemarker.directives.qianbao;

import com.zero2ipo.car.vipcoupon.bizc.IVipCouponService;
import com.zero2ipo.car.vipcoupon.bo.VipCoupon;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.utils.DateUtil;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 我的优惠券列表
 * @author zhengYunfei
 *
 */
public class MyVipCouponListDirective implements TemplateDirectiveModel{
	private static final String USERID="userId";
	private static final String PARAM_STATUS="status";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<VipCoupon> list=null;
		try {
			String userId=DirectiveUtils.getString(USERID, params);
			String status=DirectiveUtils.getString(PARAM_STATUS, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(!StringUtil.isNullOrEmpty(userId)){
				queryMap.put("userId", userId);
			}
			list=vipCouponService.findAllList(queryMap);
			for(int i=0;i<list.size();i++){
			 String endTime=list.get(i).getCouponEndTime();
			 String currentDate=DateUtil.getCurrentDateStr().substring(0,10);
			    Date startDate=StringUtil.str2SqlDate(currentDate,"yyyy-MM-dd");
				if(!StringUtil.isNullOrEmpty(endTime)){
					Date endDate=StringUtil.str2SqlDate(endTime, "yyyy-MM-dd");
					int start=startDate.getDate();
					int end=endDate.getDate();
					int value=end-start;
					if(value<0){//已过期
						list.get(i).setStatus("2");
					}
				}

			}
			env.setVariable("vipCouponList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));

		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "vipCouponService")
	private IVipCouponService vipCouponService;
}
