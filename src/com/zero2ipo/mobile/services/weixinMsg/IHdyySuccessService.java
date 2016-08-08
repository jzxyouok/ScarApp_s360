package com.zero2ipo.mobile.services.weixinMsg;
import com.zero2ipo.module.entity.user.UserEntity;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 活动预约成功发送微信消息接口
 * @author zhengyunfei
 */
public interface IHdyySuccessService {
	public boolean sendWeixin(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 获取模板数据
	 * @return
	 */
	public WxTemplate getWxTemplate(String openId, String msgTemplateId, UserEntity user);
}
