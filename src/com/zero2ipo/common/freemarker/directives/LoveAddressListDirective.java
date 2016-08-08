package com.zero2ipo.common.freemarker.directives;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zero2ipo.common.entity.Address;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.core.MobileUserContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.bsb.IAddressService;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * 我的订单标签
 * @author zhengYunfei
 *
 */
public class LoveAddressListDirective implements TemplateDirectiveModel{
	private static final String OPEN_ID="openid";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<Address> list=null;
		try {
			String openId=DirectiveUtils.getString(OPEN_ID, params);
			Map<String,Object> queryMap=new HashMap<String,Object>();
			if(StringUtil.isNullOrEmpty(openId)){
				openId=MobileUserContants.DEFAULT_OPEN_ID;
			}
			queryMap.put("openId", openId);
			list=addressService.findAllList(queryMap);
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				env.setVariable("addressList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "addressService")
	private IAddressService addressService;
}
