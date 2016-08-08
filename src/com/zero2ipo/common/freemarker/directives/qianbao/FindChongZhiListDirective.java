package com.zero2ipo.common.freemarker.directives.qianbao;

import com.zero2ipo.car.chongzhi.bizc.IChongZhi;
import com.zero2ipo.car.chongzhi.bo.ChongZhiBo;
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
 * 余额充值标签
 * @author zhengYunfei
 */
public class FindChongZhiListDirective implements TemplateDirectiveModel{
	private final static String PARAM_VALUE="count";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<ChongZhiBo> list=null;
		try {
			Map<String,Object> queryMap=new HashMap<String,Object>();
			String count= DirectiveUtils.getString(PARAM_VALUE, params);
			if(!StringUtil.isNullOrEmpty(count)){
				int size=Integer.parseInt(count);
				queryMap.put("count",size);
			}
			list=chongzhi.findAllList(queryMap);
			if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
				env.setVariable("chongzhiList", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "chongzhi")
	private IChongZhi chongzhi;
}
