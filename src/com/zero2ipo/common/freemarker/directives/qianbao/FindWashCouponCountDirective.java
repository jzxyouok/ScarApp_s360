package com.zero2ipo.common.freemarker.directives.qianbao;

import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.mobile.services.bsb.IWashCouponService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取洗车券张数标签
 * @author zhengYunfei
 */
public class FindWashCouponCountDirective implements TemplateDirectiveModel{
	private static final String PARAM_CODE_VALUE = "userId";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		int count=0;
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			String userId= DirectiveUtils.getString(PARAM_CODE_VALUE, params);
			queryMap.put("userId",userId);
			count=washCouponService.findAllListCount(queryMap);
			env.setVariable("washCouponCount", ObjectWrapper.DEFAULT_WRAPPER.wrap(count));

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
