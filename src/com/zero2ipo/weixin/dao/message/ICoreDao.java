package com.zero2ipo.weixin.dao.message;

import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2015/7/16.
 */
@Repository
public interface ICoreDao {
    public  String processRequest(HttpServletRequest request, HttpServletResponse response) ;
    public String getValue(String key);
}
