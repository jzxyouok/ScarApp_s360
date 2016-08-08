package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.car.project.bizc.IServiceProject;
import com.zero2ipo.car.project.bo.ServiceProject;
import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.app.Goodses;
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
 * 服务项目列表
 * @author zhengYunfei
 *
 */
public class ServiceProjectListDirective implements TemplateDirectiveModel{

	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Goodses> list=null;
		try {
			Map<String,Object> queryMap=new HashMap<String, Object>();
			list=serviceProject.findAllList(queryMap);
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				env.setVariable("fwxmList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "serviceProject")
	private IServiceProject serviceProject;
}
