package com.zero2ipo.common.web;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @title :web层基类
 * @author: zhengYunFei
 * @data: 2013-7-1
 */
public class BaseCtrl {
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpSession session;
    // do some thiing...
    protected HttpServletResponse response;
}
