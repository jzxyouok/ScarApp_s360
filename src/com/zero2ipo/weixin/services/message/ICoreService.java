package com.zero2ipo.weixin.services.message;
import com.zero2ipo.weixin.templateMessage.WxTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by ZhengYunfei on 2015/7/16
 * 微信发送消息核心接口
 */
@Service
public interface ICoreService {
    /**
     * 微信发送普通消息接口
     * @param request
     * @param response
     * @return
     */
    public  String processRequest(HttpServletRequest request, HttpServletResponse response) ;
    /**
     * 发送模板消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     * msgTemplateId 模板ID
     */
    public int send_template_message(String appId, String appSecret, String openId, WxTemplate template);

    /**
     * 发送普通消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     * msgTemplateId 模板ID
     */
    public void send_message(String appId, String appSecret, String openId, String msgContent);
    /**
     * 获取微信关注者openid
     * @return
     */
    public String getOpenId(HttpServletRequest request, HttpServletResponse response);
    public String getValue(String key);

}
