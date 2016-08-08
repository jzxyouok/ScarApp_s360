package com.zero2ipo.common;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * @title :公共参数类
 * @author: zhengYunFei
 * @data: 2010-8-2
 */
public class GlobalConstant {
    private GlobalConstant(){};
    
    //登录用户常量
	public static final String SESSION_USER = "sessionUser";
	
    //超级管理员ID
    public static final String SUPER_OPER_ID = "1";
	
    public static final String PROJECT_CLASS_PATH = GlobalConstant.class.getClassLoader().getResource("/").getFile();//项目类的根路径
    public static final String PROJECT_ROOT_PATH      = PROJECT_CLASS_PATH.replaceAll("WEB-INF/.*", "");//项目根路径
    public static final String PRO_FILE_CONFIG        = "config";// 项目配置文件名 config
    
    public static final String VERIVYWORD = "verifyWord";//验证码
    
    public static ExecutorService uploadPool = Executors.newFixedThreadPool(20);//全局线程池一 文件上传处理类,RSSTask

    public static ExecutorService sendMailPool = Executors.newFixedThreadPool(80);//全局线程池一 邮件发送
    
    public static final String TOP_SYSMENU_CODE = "999999";   //顶级菜单
    
    public static final String USER_OPER_ENABLE = "1" ;  //用户启用操作标识 
   
    public static final String USER_OPER_DISABLE= "0" ;  //用户停用操作标识
    
	public static final String PCODE_INFOSTATUS_TRUE = "1";         //是，有效，生效，在线
    public static final String PCODE_INFOSTATUS_FALSE = "0";        //否，无效，注销，离线
    
    
    public static final String menu_Folder_Flag = "1"; //菜单夹
    public static final String menu_Option_Flag = "2"; //菜单项
    
    public static final String TREE_ROOT="999999";//根节点 组织机构   标准代码项一级几点  
    
	public static final String YYMM = "yyyyMM" ;	//时间格式
	
    public static final String BIRTHDAY_INIT = "9999-12-24" ;  //初始化用户生日日期
    
    public static final String USER_INITPHOTO = "/phone/res/img/touxiang.jpg" ;	//注册用户默认用户头像
    
    public static final String PHONE_IMGPATH = "/phone/res/file/img/" ; //客户端手机图片上传路径
    //字典表常量
    public static final String USER_APPLY_STATUS="用户申请状态";    
    public static final String VIP_STATUS="会员状态";
    public static final String VIP_RANK="会员等级";
    
    //文章状态
    public static final String ARTICLE_STATUS="文章状态";
    //文章导航
    public static final String ARTICLE_NAVIGATION="文章导航";
	//上传文件类型
	public static final String ATTACHMENT_TYPE_IMAGE = "image";
	public static final String ATTACHMENT_TYPE_DOCUMENT = "document";
	public static final String ATTACHMENT_TYPE_OTHER = "other";
	/**机构常量 **/
	public static final String NORMAL = "01";

	public static final String YYMMDD = "YYMMDD";
	//自定义配置文件
	public static final String ENVIRONMENT_PROPERTIES_PATH = "config.properties";
	
	//增加编码常量
	public static final String UTF8 = "UTF-8";

	//站点key
	public static final String SITE_KEY = "_site_key";
	
	//用户key
	public static final String USER_KEY = "_user_key";
	
	//基金
	public static final String FORTUNE = "FORTUNE";
	
	//产品
	public static final String PRODUCT = "PRODUCT";
	//关于页面
	public static final String ABOUT = "ABOUT";
	
	public static final String PRESENTATION  = "PRESENTATION";
	
	//存储验证码的key
	public static final String VERIFICATION_CODE = "_code";
	
	//优选产品
	public static final String GOOD_PRO="103";
	//成功案例
	public static final String SUSSESSFUL_CASE="108";
	
	public static final String SORT_PRO="122";
	//投资领域
	public static final String INVESTMENT_FIELD="101";
	
	//产品：1、基本信息；2：产品特点；3：产品收益测算 &&
	//基金：4：产品概要；5、费用及分配；6：基金投资事宜
	public static final String OBJECT_1="1";
	public static final String OBJECT_2="2";
	public static final String OBJECT_3="3";
	public static final String OBJECT_4="4";
	public static final String OBJECT_5="5";
	public static final String OBJECT_6="6";
	//网站短信提示语
	public static final String  MESSAGE_INFO="120";
	public static final String MESSAGE_INVINATION_CODE="5";//邀请码短信提示语
	//前端域名
	public static final String WEBSITE_DOMAIN_PRODUCT="http://www.pestreet.cn/";
	public static final String WEBSITE_DOMAIN_NAME=WEBSITE_DOMAIN_PRODUCT+"article/";
	public static final String WEBSITE_DOMAIN_NAME_LIST=WEBSITE_DOMAIN_PRODUCT+"articlelist/";
	public static final String WEBSITE_DOMAIN_NAME_Suffix=".html";
	//后台项目根路径
	public static final String PROJECT_ROOT_PATH_NAME="/c";
	
	//存放跳转路径的key
	public static final String REVOLUTION_KEY = "_revolution_key";
	
	//用户类型
	public static final String USER_TYPE = "USERINFO";				//个人注册用户
	
	public static final String ORG_USER_TYPE = "ORGUSERINFO";		//机构注册用户
	
	public static final String CRM_USER_TYPE = "CRMINFO";			//CRM关联用户
	//会员状态
	public static final String USER_VIP_STATUS_00="0";
	public static final String USER_VIP_STATUS_NAME_00="信息未完善";
	public static final String USER_VIP_STATUS_01="1";
	public static final String USER_VIP_STATUS_NAME_01="待审核";
	public static final String USER_VIP_STATUS_02="2";
	public static final String USER_VIP_STATUS_NAME_02="精英会员";
	public static final String USER_VIP_STATUS_03="3";
	public static final String USER_VIP_STATUS_NAME_03="审核不通过";
	public static final String USER_VIP_STATUS_04="4";
	public static final String USER_VIP_STATUS_NAME_04="冻结";
	public static final String USER_VIP_STATUS_05="5";
	public static final String USER_VIP_STATUS_NAME_05="非会员";
	public static final Map<String,Object> userStatusMap=new HashMap<String, Object>();
	//我的收藏类型
	public static final String MY_COLLECTION_TYPE_0="0";//基金
	public static final String MY_COLLECTION_TYPE_1="1";//文章
	public static final String MY_COLLECTION_TYPE_2="2";//活动
	public static final String MY_HUIYUAN_HUODONG_TYPE="1021403";//会员活动codevalue
	public static final String MY_TYPE_VALUE_SYS_CODE="12201";//基金产品排序

	

}

