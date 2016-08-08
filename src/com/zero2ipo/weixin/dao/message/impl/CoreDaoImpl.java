package com.zero2ipo.weixin.dao.message.impl;

import com.thoughtworks.xstream.XStream;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.ConfValue;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.services.config.IConfManage;
import com.zero2ipo.mobile.services.pic.IPictureService;
import com.zero2ipo.module.entity.picture.PictureEntity;
import com.zero2ipo.weixin.config.Config;
import com.zero2ipo.weixin.dao.message.ICoreDao;
import com.zero2ipo.weixin.msg.*;
import com.zero2ipo.weixin.test.WeixinUtil;
import com.zero2ipo.weixin.token.AccessToken;
import com.zero2ipo.weixin.token.TokenThread;
import com.zero2ipo.weixin.utils.MessageUtil;
import com.zero2ipo.weixin.utils.SerializeXmlUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by Administrator on 2015/7/16.
 */
@Component("coreDao")
public class CoreDaoImpl extends IbatisBaseDao implements ICoreDao{
	 @Resource(name = "confManage")
	 private IConfManage confManage;
    @Resource(name="pictureService")
     private IPictureService pictureService;
    /**
     * 处理微信发来的请求
     * @param request
     * @return xml
     */
    public  String processRequest(HttpServletRequest request,HttpServletResponse response) {
        String respMessage = "";
        try {
            // 默认返回的文本消息内容
            String respContent = "";

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            String content=requestMap.get("Content");
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            //textMessage.setFuncFlag(0);
            // 创建图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            // newsMessage.setFuncFlag(0);
            List<Article> articleList = new ArrayList<Article>();
            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                if(content.equals("zhengyunfei")){
                    Article article = new Article();
                    article.setTitle("文章标题");
                    article.setDescription("");
                    article.setPicUrl("http://www.pestreet.cn/c/freemarker/upload/img/20150618/20150618142923banner.jpg");
                    article.setUrl("http://www.baidu.com");
                    articleList.add(article);
                    // 设置图文消息个数
                    newsMessage.setArticleCount(articleList.size());
                    // 设置图文消息包含的图文集合
                    newsMessage.setArticles(articleList);
                    // 将图文消息对象转换成xml字符串
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                }
                if(content.equals("员工登陆")||content.equals("1")){
                    //respContent="";
                    String domain=getValue(CodeCommon.DOMAIN);
                    String appId=getValue(CodeCommon.APPID);
                    String url= "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state=210#wechat_redirect";
                   // respContent="<a href=\""+domain+"/adminLogin.html\">员工入口</a>";
                    respContent="<a href=\""+url+"\">员工入口</a>";

                }

            }
            // 图片消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                //respContent += "您发送的是图片消息！";
                //respContent="";

            }
            // 地理位置消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                //respContent += "您发送的是地理位置消息！";
            }
            // 链接消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                // respContent += "您发送的是链接消息！";
            }
            // 音频消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                //respContent += "您发送的是音频消息！";
            }
            // 事件推送
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get("Event");
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                   String domain  =getValue(CodeCommon.DOMAIN);
                    String appId=getValue(CodeCommon.APPID);
                   Article article = new Article();
                    article.setTitle("注册即可免费参与幸运大转盘抽奖");
                    article.setDescription("");
                    article.setPicUrl("http://mmbiz.qpic.cn/mmbiz/yn5O41nyIFNBpou5icS5TgKXugdR3OAGiabXeI36MXdjSZgia4e6st1LvHXdPBE8PibJlfaALUuGqkW26NQvP0cdBQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5");
                   String url= "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+domain+"/oauth/do.html&response_type=code&scope=snsapi_base&state=320#wechat_redirect";
                   // article.setUrl(domain+"/hd/dazhuanpan.html");
                    article.setUrl(url);
                    articleList.add(article);
                    Article article2 = new Article();
                    article2.setTitle("欢迎关注小海豚上门洗车服务平台");
                    article2.setDescription("");
                    article2.setPicUrl("https://mmbiz.qlogo.cn/mmbiz/yn5O41nyIFPRlMsiayZ3V15sWKrIUhOlWlHOZvaCQS9eibftENgAbwz2YfBGMtW9unX6iciacotibKfqQhLj8g514iaQ/640?wx_fmt=jpeg&tp=webp&wxfrom=5");
                    article2.setUrl("http://mp.weixin.qq.com/s?__biz=MzI4NzMwNTUzMA==&mid=100000012&idx=1&sn=13c95dbd2e8fe27a174dd9611951abea#wechat_redirect");
                    articleList.add(article2);
                    // 设置图文消息个数
                    newsMessage.setArticleCount(articleList.size());
                    // 设置图文消息包含的图文集合
                    newsMessage.setArticles(articleList);
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    // 将图文消息对象转换成xml字符串
                   /* respContent=getValue(CodeCommon.WelcomeValue);*/


                }
                // 取消订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = requestMap.get("EventKey");
                    System.out.println("eventKey==========================="+eventKey);
                    if(eventKey.equals("2")){
                    	respContent=getValue(CodeCommon.CLICK_KEY_ZHAOGONG);
                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_1)){
                        respContent=getValue(CodeCommon.CLICK_KEY_1);
                        System.out.println("resContent==========================="+respContent);
                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_2)){
                        respContent=getValue(CodeCommon.CLICK_KEY_2);
                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_3)){
                        respContent=getValue(CodeCommon.CLICK_KEY_3);
                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_4)){
                        String picName=getValue(CodeCommon.CLICK_KEY_4);
                        //发送图文消息1
                        List<PictureEntity> list=pictureService.findPicturesByTypeName(picName);
                        if(null!=list&&list.size()>0){
                            PictureEntity entity=list.get(0);
                            String title=entity.getName();
                            String remark=entity.getRemark();
                            String picUrl=entity.getAttachmentUrl();
                            String url=entity.getUrl();
                            Article article = new Article();
                            article.setTitle(title);
                            article.setDescription(remark);
                            article.setPicUrl(picUrl);
                            article.setUrl(url);
                            articleList.add(article);
                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);
                        }


                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_5)){
                        String picName=getValue(CodeCommon.CLICK_KEY_5);
                        //发送图文消息1
                        List<PictureEntity> list=pictureService.findPicturesByTypeName(picName);
                        if(null!=list&&list.size()>0){
                            PictureEntity entity=list.get(0);
                            String title=entity.getName();
                            String remark=entity.getRemark();
                            String picUrl=entity.getAttachmentUrl();
                            String url=entity.getUrl();
                            Article article = new Article();
                            article.setTitle(title);
                            article.setDescription(remark);
                            article.setPicUrl(picUrl);
                            article.setUrl(url);
                            articleList.add(article);
                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);
                        }
                    }
                    if(eventKey.equals(CodeCommon.CLICK_KEY_6)){
                        String picName=getValue(CodeCommon.CLICK_KEY_6);
                        //发送图文消息1
                        List<PictureEntity> list=pictureService.findPicturesByTypeName(picName);
                        if(null!=list&&list.size()>0){
                            PictureEntity entity=list.get(0);
                            String title=entity.getName();
                            String remark=entity.getRemark();
                            String picUrl=entity.getAttachmentUrl();
                            String url=entity.getUrl();
                            Article article = new Article();
                            article.setTitle(title);
                            article.setDescription(remark);
                            article.setPicUrl(picUrl);
                            article.setUrl(url);
                            articleList.add(article);
                            // 设置图文消息个数
                            newsMessage.setArticleCount(articleList.size());
                            // 设置图文消息包含的图文集合
                            newsMessage.setArticles(articleList);
                            // 将图文消息对象转换成xml字符串
                            respMessage = MessageUtil.newsMessageToXml(newsMessage);
                        }
                    }

                }
            }
            if(!StringUtil.isNullOrEmpty(respContent)) {
                textMessage.setContent(respContent);
                respMessage = MessageUtil.textMessageToXml(textMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
    public  String processRequest2(HttpServletRequest request,HttpServletResponse response) {
        String respMessage = "";
        try {
            // 默认返回的文本消息内容
            String respContent = "";
            respContent = "欢迎关注财富街（<a href=\"www.pestreet.cn\">www.pestreet.cn</a>）\n" +
                    "— 高端资产配置专家—\n" +
                    "\n" +
                    "财富街隶属清科集团，致力于为高净值人士和机构投资者会员提供私募股权及定增等系列高收益产品服务，以及系统的产品设计、风险管理、投资者关系沟通及管理服务，以满足高端客户资产配置和财富管理需求。\n" +
                    "\n" +
                    "关于财富街的更多高端资产配置服务 请详询 <a href=\"tel:4006547828\">4006547828</a>\n" +
                    "\n" +
                    "回复数字1-5，立即查看精华资讯。\n" +
                    "\n" +
                    "回复财富街，了解更多详情。";

            // 处理接收消息
            ServletInputStream in = request.getInputStream();
            // 将POST流转换为XStream对象
            XStream xs = SerializeXmlUtil.createXstream();
            xs.processAnnotations(InputMessage.class);
            xs.processAnnotations(OutputMessage.class);
            // 将指定节点下的xml节点数据映射为对象
            xs.alias("xml", InputMessage.class);
            // 将流转换为字符串
            StringBuilder xmlMsg = new StringBuilder();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                xmlMsg.append(new String(b, 0, n, "UTF-8"));
            }
            // 将xml内容转换为InputMessage对象
            InputMessage inputMsg = (InputMessage) xs.fromXML(xmlMsg.toString());
            String servername = inputMsg.getToUserName();// 服务端
            String custermname = inputMsg.getFromUserName();// 客户端
            long createTime = inputMsg.getCreateTime();// 接收时间
            Long returnTime = Calendar.getInstance().getTimeInMillis() / 1000;// 返回时间
            respContent=inputMsg.getContent();
           // System.out.println("开发者微信号：" + inputMsg.getToUserName());
            String openId=inputMsg.getFromUserName();
            //if(!StringUtil.isNullOrEmpty(openId)){
             //   System.out.println("保存session openid===="+openId);
             //   SessionHelper.setAttribute(request,MobileContants.USER_OPEN_ID_KEY, openId);
           // }
            //System.out.println("发送方帐号：" + inputMsg.getFromUserName());
           // System.out.println("消息创建时间：" + inputMsg.getCreateTime() + new Date(createTime * 1000l));
           // System.out.println("消息内容：" + inputMsg.getContent());
           // System.out.println("消息Id：" + inputMsg.getMsgId());
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            // 发送方帐号（open_id）
            String fromUserName =inputMsg.getFromUserName();
            // 公众帐号
            String toUserName = inputMsg.getToUserName();
            // 消息类型
            String msgType = inputMsg.getMsgType();
            // 回复文本消息
            TextMessage textMessage = new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUserName);
            textMessage.setCreateTime(new Date().getTime());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            //textMessage.setFuncFlag(0);
            // 创建图文消息
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
            // newsMessage.setFuncFlag(0);
            List<Article> articleList = new ArrayList<Article>();
            // 文本消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                if(inputMsg.getContent().equals("zhengyunfei")){
                   /** Article article = new Article();
                    article.setTitle("乐资会");
                    article.setDescription("乐资会描述");
                    article.setPicUrl("http://www.pestreet.cn/c/freemarker/upload/img/20150618/20150618142923banner.jpg");
                    article.setUrl("http://www.pestreet.cn/articlelist/jshyhd/1/70.html");
                    articleList.add(article);
                    // 设置图文消息个数
                    newsMessage.setArticleCount(articleList.size());
                    // 设置图文消息包含的图文集合
                    newsMessage.setArticles(articleList);
                    // 将图文消息对象转换成xml字符串
                    respMessage = MessageUtil.newsMessageToXml(newsMessage);
                    return respMessage;
                    **/
                    respMessage="<h1>活动预约通知</h1>\r\n\r\n"+
                            "活动名称:<font color=\"blue\">投资移民沙龙</font>\r\n"+
                            "预约结果：<font color=‘red’>成功</font>\r\n"+
                            "活动时间:<font color=‘blue’>2015-08-21 14：30</font>\r\n"+
                            "活动地点:<font color=‘blue’>北京清科集团总部\r\n"+
                            "\r\n"+
                            "您的预约已经成功，请耐心等待，或致电<a ’tel:4006547828’>4006547828</a>\r\n"+
                            "<hr>\r\n"+
                            "<a href=‘http://www.pestreet.cn/mobile’>详情</a>\r\n";

                }
                if(inputMsg.getContent().equals("888888")){
                    respMessage="<h1>活动预约通知</h1>\r\n\r\n"+
                    "活动名称:<font color=\"blue\">投资移民沙龙</font>\r\n"+
                    "预约结果：<font color=‘red’>成功</font>\r\n"+
                    "活动时间:<font color=‘blue’>2015-08-21 14：30</font>\r\n"+
                    "活动地点:<font color=‘blue’>北京清科集团总部\r\n"+
                    "\r\n"+
                    "您的预约已经成功，请耐心等待，或致电<a ’tel:4006547828’>4006547828</a>\r\n"+
                    "<hr>\r\n"+
                    "<a href=‘http://www.pestreet.cn/mobile’>详情</a>\r\n";
                }
            }
            // 图片消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                //respContent += "您发送的是图片消息！";
                respMessage="<h1>活动预约通知</h1>\r\n\r\n"+
                        "活动名称:<font color=\"blue\">投资移民沙龙</font>\r\n"+
                        "预约结果：<font color=‘red’>成功</font>\r\n"+
                        "活动时间:<font color=‘blue’>2015-08-21 14：30</font>\r\n"+
                        "活动地点:<font color=‘blue’>北京清科集团总部\r\n"+
                        "\r\n"+
                        "您的预约已经成功，请耐心等待，或致电<a ’tel:4006547828’>4006547828</a>\r\n"+
                        "<hr>\r\n"+
                        "<a href=‘http://www.pestreet.cn/mobile’>详情</a>\r\n";

            }
            // 地理位置消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                //respContent += "您发送的是地理位置消息！";
            }
            // 链接消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
               // respContent += "您发送的是链接消息！";
            }
            // 音频消息
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                //respContent += "您发送的是音频消息！";
            }
            // 事件推送
            if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = inputMsg.getEvent();
                // 订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                                       textMessage.setContent(respContent);
                    respMessage = MessageUtil.textMessageToXml(textMessage);
                    send_message(Config.APPID, Config.SECRET,openId,respMessage);

                }
                // 取消订阅
                if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                    // 事件KEY值，与创建自定义菜单时指定的KEY值对应
                    String eventKey = inputMsg.getEventKey();
                }
            }
            //textMessage.setContent(respContent);
            //respMessage = MessageUtil.textMessageToXml(textMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }
    /**
     * 发送普通消息
     * appId 公众账号的唯一标识
     * appSecret 公众账号的密钥
     * openId 用户标识
     * msgTemplateId 模板ID
     */
    public void send_message(String appId, String appSecret, String openId,String msgContent){
        //Token token = CommonUtil.getToken(appId, appSecret);
        AccessToken token=TokenThread.accessToken;
        String access_token = token.getToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+access_token;
        JSONObject jsonObject = WeixinUtil.httpRequest(url, "POST", msgContent);
        int result = 0;
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
            }
        }
    }
    /**
	 * 根据key获取value
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		String result="";
		Map<String, Object> m=new HashMap<String, Object>();
		 m.put("confKey",key);
		ConfValue  confValue=confManage.findConfValueByMap(m);
		result=confValue.getConfValue();
		return result;
	}
}
