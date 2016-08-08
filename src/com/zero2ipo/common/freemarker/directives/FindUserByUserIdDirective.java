package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.User;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.module.entity.user.UserEntity;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 根据userid查询用户信息
 * @author zhengYunfei
 *
 */
public class FindUserByUserIdDirective implements TemplateDirectiveModel{
	private static final String PARAM_ID = "userId";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Users user=null;
		String userId= DirectiveUtils.getString(PARAM_ID, params);
		try {
			if(!StringUtil.isNullOrEmpty(userId)){
				user=userServices.findUserByUserId(userId);
				if(!StringUtil.isNullOrEmpty(user)){
					env.setVariable("account", ObjectWrapper.DEFAULT_WRAPPER.wrap(user.getAccount()+""));
				}
			}
			env.setVariable("user", ObjectWrapper.DEFAULT_WRAPPER.wrap(user));
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "userServices")
	private IUserServices userServices;
}
