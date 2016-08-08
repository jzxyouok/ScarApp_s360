/**
 * Copyright (c) 2010 CEPRI,Inc.All rights reserved.
 * Created by 2010-7-16
 */
package com.zero2ipo.common.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @title :系统过滤器,并控制cookie读写
 * @author: zhengYunFei
 * @data: 2013-7-1
 */
public class SecFilter implements Filter {
    private static String urls;
    private static String path;
    /**
     * @title:参数初始化
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        urls = filterConfig.getInitParameter("urls");
    }

    /**
     * @title: 系统初始化
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri =  req.getRequestURI();
        req.setAttribute("path", req.getContextPath());
        //过滤器器中维护的特定url和移动Phone模块中的jsp页面不进行拦截
        if(!uri.matches(urls)){
            //如果没有登陆，或者请求session超时都返回重新登陆
            Object so = req.getSession().getAttribute("user");
            if(so == null || so.equals("")){
                PrintWriter out = res.getWriter() ;
                out.print("<html>") ;
                out.print("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />") ;
                out.print("<script> ") ;
                out.print("window.top.location='../s9/index.html?errorType=7';") ;
                out.print("</script>") ;
                out.print("</html>") ;
                return;
            }else{//已经登录的用户，防止其越权，访问其没有权限的url
            	//根据此登录用户的ID查询该用户所拥有的url权限

            }
         }
        chain.doFilter(request, response);
    }

    /**
     * @title:系统关闭时的资源释放操作
     */
    public void destroy() {

    }
}
