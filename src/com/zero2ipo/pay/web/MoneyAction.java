package com.zero2ipo.pay.web;

import com.zero2ipo.car.userchongzhi.bizc.IUserChongZhi;
import com.zero2ipo.car.userchongzhi.bo.UserChongZhiBo;
import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.core.MobilePageContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.pay.model.MdlPay;
import com.zero2ipo.pay.service.WXPay;
import com.zero2ipo.pay.service.WXPrepay;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by zhengyufnei on 2015/8/31.
 * 钱包
 */
@Controller
public class MoneyAction {
	public final static String notifyUrl="/order/yuepayupd.html";
	@Resource(name = "userchongzhi")
	private IUserChongZhi userchongzhi ;
	@Resource(name = "userServices")
	private IUserServices userServices ;

	/**
	 * 我的钱包页面
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/mymoney.html")
	public ModelAndView mymoney(HttpServletRequest request, HttpServletResponse response, ModelMap model)
	{
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView(MobilePageContants.MY_MONEY_PAGE);
		//从全局缓存中获取当前登陆的账号
		Users current= (Users) request.getSession().getAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(current)){
			mv.addObject("account",current.getAccount());
		}

		return mv;
	}
	/**
	 * 我的优惠券
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/mycoupon.html")
	public ModelAndView mycoupon(HttpServletRequest request, HttpServletResponse response, ModelMap model,String status,Car car)
	{
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView(MobilePageContants.MY_COUPON_PAGE);
		if(StringUtil.isNullOrEmpty(status)){
			status=MobileContants.status_0;//默认显示未使用
		}
		if(!StringUtil.isNullOrEmpty(car)){
			SessionHelper.setAttribute(request, MobileContants.CAR_SESSION_KEY, car);
		}
		mv.addObject("status",status);
		return mv;
	}

	/**
	 * 我的红包页面
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/myhongbao.html")
	public ModelAndView myhongbao(HttpServletRequest request, HttpServletResponse response, ModelMap model)
	{
		ModelAndView mv=new ModelAndView(MobilePageContants.MY_HONGBAO_PAGE);
		FmUtils.FmData(request, model);
		return mv;
	}
	/**
	 * 充值
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/chongzhi.html")
	public ModelAndView chongzhi(HttpServletRequest request, HttpServletResponse response, ModelMap model)
	{
		ModelAndView mv=new ModelAndView(MobilePageContants.MY_CHOGNZHI_PAGE);
		FmUtils.FmData(request, model);
		return mv;
	}
	/**
	 * 充值
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/chongzhiWxPay.html")
	@ResponseBody
	public String chongzhiWxPay(HttpServletRequest request, HttpServletResponse response, ModelMap model,UserChongZhiBo bo)
	{
		//ModelAndView mv=new ModelAndView(MobilePageContants.MY_HONGBAO_PAGE);
		//FmUtils.FmData(request, model);
		//首先获取当前登陆的会员信息
		Users u= (Users) SessionHelper.getAttribute(request,MobileContants.USER_SESSION_KEY);
		if(!StringUtil.isNullOrEmpty(u)){
			String userId=u.getUserId();
			bo.setUserId(userId);
		}
		//只有付款成功之后，才保存到数据库中，所以现在需要将充值保存到缓存中
		ServletContext application =request.getSession().getServletContext();
		application.setAttribute(MobileContants.CURRENT_CHONGZHI_KEY,bo);
		String jsParam=getWXJsParamForNative(request,bo.getMoney());
		//mv.addObject("jsParam",jsParam);
		//userchongzhi.add(bo);
		return jsParam;
	}
	/**
	 * 充值
	 * @author zhengyunfei
	 * */
	@RequestMapping(value = "/order/savechongzhi.html")
	public ModelAndView saveChongZhi(HttpServletRequest request, HttpServletResponse response, ModelMap model)
	{
		ModelAndView mv=new ModelAndView(MobilePageContants.MY_MONEY_PAGE);
		FmUtils.FmData(request, model);
		ServletContext application =request.getSession().getServletContext();
		UserChongZhiBo bo= (UserChongZhiBo) application.getAttribute(MobileContants.CURRENT_CHONGZHI_KEY);
		if(!StringUtil.isNullOrEmpty(bo)){
			userchongzhi.add(bo);
			//同时更新用户钱包数量
			//根据用户id更新钱包余额ff
			String userId=bo.getUserId();
			Users u=new Users();
			u.setUserId(userId);
			u.setAccount(bo.getMoney() + bo.getZsmoney());
			userServices.updateUserQianBao(u);
			//付款成功之后将当前缓存key清楚
			application.removeAttribute(MobileContants.CURRENT_CHONGZHI_KEY);
		}
		return mv;
	}
	//获取微信支付参数信息
	public String getWXJsParamForNative(HttpServletRequest request, float total_free) {
		Map<String, Object> result = new HashMap<String, Object>();
		MdlPay pay = new MdlPay();
		WXPrepay prePay = getWxPrepay(request);
		float b = (float) (Math.round(total_free * 100)) / 100;
		prePay.setTotal_fee((int) (b * 100) + "");
		prePay.setTrade_type("JSAPI");
		String jsParam = "";
		//此处添加获取openid的方法，获取预支付订单需要此参数！！！！！！！！！！！
		// 获取预支付订单号
		String prepayid = prePay.submitXmlGetPrepayId();
		if (!"".equals(prepayid) && prepayid != null && prepayid.length() > 10) {
			// 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
			jsParam = WXPay.createPackageValue(prePay.getAppid(), prePay.getPartnerKey(), prepayid);
		}
		return jsParam;
	}
	/**
	 * 动态获取wxPrepay
	 */
	private WXPrepay getWxPrepay(HttpServletRequest request) {
		ServletContext application =request.getSession().getServletContext();
		String partnerId =application.getAttribute(MobileContants.PARTNERID_KEY)+"";
		String appid =application.getAttribute(MobileContants.APPID_KEY)+"";
		String partnerValue = application.getAttribute(MobileContants.PARTNERVALUE_KEY)+"";
		String spbill_create_ip = request.getRemoteAddr();
		WXPrepay prePay = new WXPrepay();
		String prePayBody = application.getAttribute(MobileContants.PREPAYBODY_KEY)+"";
		String domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";
		prePay.setAppid(appid);
		prePay.setBody(prePayBody);
		prePay.setPartnerKey(partnerValue);
		prePay.setMch_id(partnerId);
		prePay.setNotify_url(domain + notifyUrl);
		String outTradeNo= UUID.randomUUID().toString().replace("-","");
		prePay.setOut_trade_no(outTradeNo);//每次重新生成交易单号，防止订单重复，但是需要把订单里面的outTradeNo也修改了
		prePay.setSpbill_create_ip(spbill_create_ip);
		String openid = "";
		//首先从当前登录的账号信息中获取openid
		Object o=  SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
		if(o instanceof Users){
			Users u= (Users) o;
			if(!StringUtil.isNullOrEmpty(u)){
				openid=u.getOpenId();//如果数据库中不存在openid，再从缓存中读取
				if(StringUtil.isNullOrEmpty(openid)){
					openid=SessionHelper.getStringAttribute(request, MobileContants.USER_OPEN_ID_KEY);//从缓存中获取，这里有时会获取不到，所有要从数据库中读取
				}
			}
		}
		prePay.setOpenid(openid);
		return prePay;
	}
	/**
	 * 微信支付回调方法统一走这里，上面的方式总是出现bug,客户付款成功之后，订单状态不改变
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/order/yuepayupd.html")
	@ResponseBody
	public ModelAndView wxpayHdMethod(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv=new ModelAndView();
		System.out.println("微信支付回调开始。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
		Map<String,Object> result=new HashMap<String, Object>();
		boolean flag=false;
		String inputLine;
		String notityXml = "";
		String resXml = "";
		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map m = parseXmlToList2(notityXml);
		String openid=m.get("openid")+"";
		String return_code=m.get("return_code")+"";//付款成功与否的标志
		String total_fee=m.get("total_fee")+"";//付款金额
		String transaction_id=m.get("transaction_id")+"";//微信支付订单号
		String out_trade_no=m.get("out_trade_no")+"";//商户订单号
		String attach=m.get("attach")+"";//商家数据包
		String time_end=m.get("time_end")+"";//支付时间
		Map<String,Object> query=new HashMap<String, Object>();
		query.put("transactionId",transaction_id);
		int count=userchongzhi.findAllListCount(query);
		//从缓存中获取订单
		ServletContext application =request.getSession().getServletContext();
		UserChongZhiBo chongZhiBo= (UserChongZhiBo) application.getAttribute(MobileContants.CURRENT_CHONGZHI_KEY);
		String url="";
		if(!StringUtil.isNullOrEmpty(chongZhiBo)){
			//	url="redirect:/my/order"+order.getId()+".html";
			mv.setViewName(url);
		}
		if(count==0){
			if(!StringUtil.isNullOrEmpty(chongZhiBo)){
				//更新订单支付状态，同时更新商户订单号和交易单号
				chongZhiBo.setOutTradeNo(out_trade_no);//根据outtradeNo查询订单信息
				if("SUCCESS".equals(return_code)){
					chongZhiBo.setTransactionId(transaction_id);
					userchongzhi.add(chongZhiBo);
					//更新钱包余额数量=充值金额+赠送金额
					float money=chongZhiBo.getMoney();
					float zsmoney=chongZhiBo.getZsmoney();
					float total=money+zsmoney;
					String userId=chongZhiBo.getUserId();
					//根据用户id更新钱包余额ff
					Users u=new Users();
					u.setUserId(userId);
					u.setAccount(total);
					userServices.updateUserQianBao(u);
					result.put("userId",userId);
					//更新缓存中钱包余额
					Users  current= (Users) application.getAttribute(MobileContants.USER_APPLICATION_SESSION_KEY);
					current.setAccount(total);
					application.setAttribute(MobileContants.USER_APPLICATION_SESSION_KEY, current);
					//付款成功之后将当前缓存key清楚
					SessionHelper.removeAttribute(request,MobileContants.CURRENT_CHONGZHI_KEY);
				}
			}
			String templateMessageId=application.getAttribute(MobileContants.PAIDAN_TEMPLATE_KEY)+"";
			//查询域名
			String  domain=application.getAttribute(MobileContants.DOMAIN_KEY)+"";//首先从缓存中获取
			//url=domain+"/renwu/order"+order.getId()+".html";
			//WxTemplate wxTemplate= TemplateMessageUtils.getPaiDanTemplate(openId, templateMessageId, url, order, bo);
			//发送模板消息
			String appId=application.getAttribute(MobileContants.APPID_KEY)+"";
			String appsecret=application.getAttribute(MobileContants.APPSCRET_KEY)+"";
			//coreService.send_template_message(appId,appsecret,openId,wxTemplate);

		}

		result.put("success",flag);
		return mv;
	}
	/**
	 * description: 解析微信通知xml
	 *
	 * @param xml
	 * @return
	 * @author ex_yangxiaoyi
	 * @see
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
	/******************************************钱包付款**********************************************************************/

}

