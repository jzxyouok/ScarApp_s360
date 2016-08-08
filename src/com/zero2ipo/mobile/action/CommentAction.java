package com.zero2ipo.mobile.action;

import com.zero2ipo.common.domain.Upload;
import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.Comment;
import com.zero2ipo.common.http.FmUtils;
import com.zero2ipo.framework.util.DateUtil;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.services.comment.ICommentService;
import com.zero2ipo.mobile.services.config.IConfManage;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.weixin.services.message.ICoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 */
@Controller
public class CommentAction {

    @Resource(name = "commentService")
    private ICommentService commentService ;
    @Resource(name = "confManage")
	private IConfManage confManage;
    @Autowired
    private IUserServices userServices;
    /*
	 * 核心服务接口注入
	 */
    @Autowired
	public ICoreService coreService;

	/*
	 * 跨域上传
	 */
	@Resource(name = "domainUpload")
	public Upload upload;

	/**
	 * update washcar before after photo
	 */
	@RequestMapping(value = "/pinglun.html", method = RequestMethod.POST)
	public String registerStep2POST(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, String userCardFile, String idCardFile,String starNum,String star2Num,String star3Num, String orderId,String content,RedirectAttributes redirectAttributes ) {
		FmUtils.FmData(request, model);
		ModelAndView mv=new ModelAndView("redirect:/renwu/order"+orderId+".html");
		 Map<String, Object> resultMap=new HashMap<String, Object>();
		 Comment comment2=new Comment();
		comment2.setCommentId(DateUtil.getCurrentDate("yyyyMMddHHmmss"));
		 comment2.setVipId(orderId);
		 comment2.setContent(content);
		comment2.setStarNum(starNum);
		comment2.setStar2Num(star2Num);
		comment2.setStar3Num(star3Num);
		 Map<String, Object> queryMap=new HashMap<String, Object>();
		 queryMap.put("orderId", orderId);
		 AdminBo adminBo=userServices.findAdminLoginMessage(queryMap);
		 if(!StringUtil.isNullOrEmpty(adminBo)){
			 comment2.setCommentId(adminBo.getUserId());
		 }
		 boolean flag=commentService.addComment(comment2);
		String url="f/order"+orderId+".html";
		return "redirect:/"+url+"?flag=success";
	}

}

