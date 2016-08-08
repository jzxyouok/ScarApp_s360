package com.zero2ipo.mobile.action;

import com.zero2ipo.core.MobileContants;
import com.zero2ipo.weixin.token.AccessToken;
import com.zero2ipo.weixin.token.TokenThread;
import com.zero2ipo.weixin.utils.GetImage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 手机端主页调整控制
 * @author zhengyunfei
 * @date 2015-04-22
 *
 */

@Controller
public class DownLoadImage {
	public  final static String DOWNLOAD_WEIXIN_IMAGE="http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
	/**
	 * download image to local server computer
	 * @param request
	 * @param response
	 * @param model
     * @return
     */
	@RequestMapping(value = "/bfpic/downimage.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> downloadImageToLocalServer(HttpServletRequest request,
														 HttpServletResponse response, ModelMap model, String media_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		System.out.println("方法进来了嘛》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》media_id==="+media_id);
		try{
			//String localPath=System.getProperty("user.dir").replace("bin", "webapps/upload/");
			//通过media_id获取网络图片url
			String appId=request.getSession().getAttribute(MobileContants.APPID_KEY)+"";
			//String appSecret=request.getSession().getAttribute(MobileContants.APPSCRET_KEY)+"";
			AccessToken token= TokenThread.accessToken;
			String access_token = token.getToken();
			System.out.println("token==========================="+access_token);
			String requestUrl =DOWNLOAD_WEIXIN_IMAGE;
			//String args[]=media_id.split(",");
			//int count=args.length;
			//for(int i=0;i<count;i++){
				//requestUrl = requestUrl.replace("ACCESS_TOKEN",access_token).replace("MEDIA_ID",media_id);
				//String base64Image= GetImage.downImageForNetUrl(requestUrl,media_id);
				String base64Image= getInputStream(media_id,access_token);
				map.put("bfPic",base64Image);
			//}
			map.put("success",true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * download image to local server computer
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/afpic/downimage.html", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> afpicDownloadImageToLocalServer(HttpServletRequest request,
														 HttpServletResponse response, ModelMap model, String media_id) {
		Map<String,Object> map=new HashMap<String, Object>();
		System.out.println("方法进来了嘛》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》media_id==="+media_id);
		try{
			//String localPath=System.getProperty("user.dir").replace("bin", "webapps/upload/");
			//通过media_id获取网络图片url
			String appId=request.getSession().getAttribute(MobileContants.APPID_KEY)+"";
			//String appSecret=request.getSession().getAttribute(MobileContants.APPSCRET_KEY)+"";
			AccessToken token= TokenThread.accessToken;
			String access_token = token.getToken();
			System.out.println("token==========================="+access_token);
			String requestUrl =DOWNLOAD_WEIXIN_IMAGE;
			String args[]=media_id.split(",");
			//int count=args.length;
			//for(int i=0;i<count;i++){
				requestUrl = requestUrl.replace("ACCESS_TOKEN",access_token).replace("MEDIA_ID",media_id);
				String base64Image= GetImage.downImageForNetUrl(requestUrl,media_id);
				//System.out.println("afPic"+i+"=================================="+base64Image);
				map.put("afPic",base64Image);
			//}
			map.put("success",true);
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}

	/**

	 * 根据文件id下载文件

	 *

	 * @param mediaId

	 *            媒体id

	 * @throws Exception

	 */

	public String getInputStream(String mediaId,String accessToken) {
		InputStream is = null;
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="
				+ accessToken + "&media_id=" + mediaId;
		try {
			URL urlGet = new URL(url);
			HttpURLConnection http = (HttpURLConnection) urlGet
					.openConnection();
			http.setRequestMethod("GET"); // 必须是get方式请求
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			http.setDoOutput(true);
			http.setDoInput(true);
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
			http.connect();
			// 获取文件转化为byte流
			is = http.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] btImg = new byte[0];//得到图片的二进制数据
		String base64Image="";
		try {
			btImg = GetImage.readInputStream(is);
			ByteArrayInputStream in = new ByteArrayInputStream(btImg);    //将b作为输入流；
			BufferedImage image = ImageIO.read(in);     //将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();
			base64Image=GetImage.getImageBinary(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base64Image;

	}



}
