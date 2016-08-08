package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IOrderService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 根据当前登陆账号的手机号码，查询该会员已完成订单的个数
 * @author zhengYunfei
 *
 */
public class FindWashCountByUserIdDirective implements TemplateDirectiveModel{
	private static final String PARAM_USERID="userId";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		int count = 0;
		try {
			String userId = DirectiveUtils.getString(PARAM_USERID, params);
			Map<String, Object> queryMap = new HashMap<String, Object>();
			if (!StringUtil.isNullOrEmpty(userId)) {
				queryMap.put("userId", userId);
				count = orderService.getTotal(queryMap);
				env.setVariable("count", ObjectWrapper.DEFAULT_WRAPPER.wrap(count));
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
