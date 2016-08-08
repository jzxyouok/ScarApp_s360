package com.zero2ipo.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
 

@Controller
@RequestMapping("/ajaxUtil")
public class AjaxUtilCtrl extends BaseCtrl{
    
    /**
     * AIAX跨域请求处理
     */
    @RequestMapping("/crossDomain")
    @ResponseBody
    public String crossDomain(String url){
        try {
//            String res = HttpUtil.sendGet(PCodeConstant.READ_LIST_URL+url, "UTF-8");
//            return res;
        } catch (Exception e) {
        }
        return "";
    }
    
}
