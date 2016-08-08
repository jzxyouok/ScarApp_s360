package com.zero2ipo.weixin.config;


/**
 * 配置文件
 * @author zhengyunfei
 * @date 2015年7月9日
 * @version 1.0
 */
public interface Config {

	// 赋权类型 
	 String grant_type = "client_credential";
	
	// 测试开发者申请的appid
	String TEST_APPID = "wxba458b8ccfd615ef";
	//正式
	String APPID = "wx889176fe2c1c0b0c";
    String PartnerId="1271833601";
    String PartnerKey="abc1abc2abc3abc4abc5abc6abc7abc8";
	// 开发者申请的secret密钥
	String SECRET = "5701ac02b5d64c4b7f420740e63cdfb4";
	String TEST_SECRET = "453a2cc14bfa6b4f3fc4b27bbc6e7c5e";
	//页面回调连接
	String CALL_BACK_LINK="http://www.pestreet.cn/bsb/oauth/do.html";
	//微信授权连接
	String OAUTH_LINK="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APPID+"&redirect_uri="+CALL_BACK_LINK+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
}
