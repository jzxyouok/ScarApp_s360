package com.zero2ipo.common.freemarker.directives;

import com.zero2ipo.common.freemarker.DirectiveUtils;
import com.zero2ipo.mobile.services.pic.IPictureService;
import com.zero2ipo.module.entity.picture.PictureEntity;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片处理驱动类
 * @author liyang
 *
 */
public class PortalPictureDirective implements TemplateDirectiveModel {

	Logger log = Logger.getLogger(PortalPictureDirective.class);
	
	private static final String PARAM_NAME = "typeName";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(Environment env, Map params, TemplateModel[] model,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		
		List<PictureEntity> pictures = new ArrayList<PictureEntity>();
		
		try {
			
			String name = DirectiveUtils.getString(PARAM_NAME, params);			//图片类型名称
			if(name != null && !"".equals(name)) 
			{
				pictures = pictureService.findPicturesByTypeName(name);
			}
			
			env.setVariable("pictures", ObjectWrapper.DEFAULT_WRAPPER.wrap(pictures));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		body.render(env.getOut());
		
	}
	
	/*
	 * 图片服务层接口注入
	 */
	@Resource(name = "pictureService")
	private IPictureService pictureService;

}
