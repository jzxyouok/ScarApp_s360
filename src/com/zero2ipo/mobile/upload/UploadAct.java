package com.zero2ipo.mobile.upload;

import com.zero2ipo.common.domain.Upload;
import com.zero2ipo.framework.util.ResponseUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 上传请求处理
 *
 * @author liyang
 *
 */
@Controller
public class UploadAct {

	private static final String UPLOAD_ERROR_MESSAGE1 = "请重新上传，上传文件时发生错误";
	private static final String UPLOAD_ERROR_MESSAGE2 = "请上传2M以内的jpg、gif、bmp、png、jpeg格式图片";

	/*
	 * 文件上传请求
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/img/upload.act", method = RequestMethod.POST)
	@ResponseBody
	public void upload(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,@RequestParam(value = "file", required = false) MultipartFile file) {
		Map<String, String> path=null;
		System.out.println("文件上传开始==============================================");
		// 上传图片到本地临时目录下
	JSONObject json = new JSONObject();
		try {
		    path = upload.fileLocationTempUpload(request, response);
			System.out.println("path=================================="+path);
			String urlPath = path.get("urlPath");
			String fileName = path.get("fileName");
			System.out.println("urlPath==============================================" + urlPath);
			System.out.println("fileName============================================"+fileName);
			json.put("result", true);
			json.put("url", urlPath);
			json.put("fileName", fileName);

		} catch (Exception e) {
			//e.printStackTrace();
			json.put("result", false);
			json.put("msg", UPLOAD_ERROR_MESSAGE2);
		} finally {
			if(null==path){
				json.put("result", false);
				json.put("msg", UPLOAD_ERROR_MESSAGE2);
			}
			ResponseUtils.renderHtml(response, json.toString());
			return;
		}

	}

	/*
	 * 跨域上传
	 */
	@Resource(name = "domainUpload")
	private Upload upload;
}
