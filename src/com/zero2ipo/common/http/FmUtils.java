package com.zero2ipo.common.http;

import com.zero2ipo.common.GlobalConstant;
import com.zero2ipo.common.entity.Site;
import com.zero2ipo.core.MobileContants;
import org.springframework.ui.ModelMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 前端处理工具类
 *
 * @author zhengyunfei
 *
 */
public class FmUtils {
	private static final String REQUEST_HTTP = "http://";
	private static final String IMAGEPATH = "/images";
	private static final String CSSPATH = "/style";
	private static final String SCRIPTSPATH = "/scripts/portal";
	private static final String RESPATH = "/res";

	//错误页面请求
	private static final String ERROR_404_PAGE = "error/404";

	//重定向标记
	private static final String PAGE_REDIRECT = "redirect:";

	/**
	 * 相应处理，添加base,csspath,scriptspath,respath路径，供前端模版使用
	 * @param request
	 * @param model
	 */
	public static void FmData(HttpServletRequest request, ModelMap model) {

		Object o = request.getAttribute(GlobalConstant.SITE_KEY);

		Site site = null;

		if (o != null && o instanceof Site) {
			site = (Site) o;
		}

		if (model != null && site != null) {
			String base=site.getContextPath();
			model.put("base", base);
			System.out.println("base======"+base);
			ServletContext application =request.getSession().getServletContext();
			String root=application.getAttribute(MobileContants.DOMAIN_KEY)+"";
			model.put("res", site.getContextPath() + RESPATH);
			model.put("images", site.getContextPath() + IMAGEPATH);
			model.put("css", site.getContextPath() + CSSPATH);
			model.put("scripts", site.getContextPath() + SCRIPTSPATH);
			//ServletContext application =request.getSession().getServletContext();
			//String cmsRep=application.getAttribute(CodeCommon.DOMAIN)+"";
			String cmsReq= root+site.getContextPath()+"";
			model.put("cmsReq",cmsReq);
		//	model.put("cmsReq", cmsRep);
			System.out.println("cmsReq==========="+cmsReq);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			String currDate = sdf.format(new Date());
			model.put("now", currDate);
		}
	}

	/**
	 * 处理html页面跳转请求
	 * @param request
	 * @param model
	 * @param rpage
	 * @return
	 */
	public static String fmHtmlPage(HttpServletRequest request, ModelMap model, String rpage) {
		FmUtils.FmData(request, model);
		return rpage;
	}

	/**
	 * 处理重定向条状请求
	 * @param request
	 * @param model
	 * @param rpage
	 * @return
	 */
	public static String fmRedirectPage(HttpServletRequest request, ModelMap model, String rpage) {

		return new StringBuffer(PAGE_REDIRECT).append(rpage).toString();
	}

	/**
	 * 处理重定向条状请求
	 * @param request
	 * @param model
	 * @param rpage
	 * @return
	 */
	public static void fmRedirectPage(HttpServletRequest request, HttpServletResponse response, ModelMap model, String rpage) throws IOException {

		response.sendRedirect(new StringBuffer(request.getContextPath()).append(rpage).toString());
	}

	/**
	 * 返回没有找到页面
	 * @param request
	 * @param model
	 * @return
	 */
	public static String fmNotFountPage(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		FmData(request, model);
		return ERROR_404_PAGE;
	}
}
