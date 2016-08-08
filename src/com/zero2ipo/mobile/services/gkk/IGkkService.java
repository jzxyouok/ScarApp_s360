package com.zero2ipo.mobile.services.gkk;

import com.zero2ipo.common.entity.app.Users;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 公开课服务层接口
 * @author zhengYunfei
 *
 */
@Service
public interface IGkkService {

	/**
	 * 公开课初始化
	 * @param model
	 * @return
	 */
	public ModelMap initGkkPage(ModelMap model);
	public ModelMap initGkkPage(ModelMap model, String openId, Users user);
	public ModelMap initMyOrder(ModelMap model, String openid, String page, Users user);

}
