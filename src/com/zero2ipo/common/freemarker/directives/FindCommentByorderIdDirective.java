package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.entity.Comment;
import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.comment.ICommentService;
import freemarker.core.Environment;
import freemarker.template.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 根据订单id查询评论内容
 * @author zhengYunfei
 *
 */
public class FindCommentByorderIdDirective implements TemplateDirectiveModel{
	private static final String ORDER_ID="orderId";
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Comment comment=null;
		try {
			String orderId=DirectiveUtils.getString(ORDER_ID, params);
			Map<String, Object> queryMap=new HashMap<String, Object>();
			if(!StringUtil.isNullOrEmpty(orderId)){
				queryMap.put("orderId", orderId);
			}
			comment=commentService.updCommentInit(queryMap);
			if(!StringUtil.isNullOrEmpty(comment)){
				env.setVariable("comment", ObjectWrapper.DEFAULT_WRAPPER.wrap(comment));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		body.render(env.getOut());
	}
	/*
	 * 收藏服务层接口注入
	 */
	@Resource(name = "commentService")
	private ICommentService commentService;
}
