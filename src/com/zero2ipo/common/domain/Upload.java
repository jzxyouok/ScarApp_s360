package com.zero2ipo.common.domain;

import com.zero2ipo.common.domain.exception.DomainFileUploadException;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.utils.SignUtils;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 跨域文件上传
 * @author zhengyunfei
 *
 */
public class Upload {

	Logger logger = Logger.getLogger(Upload.class);

	private String fileMax = "50";
	private String domainUrl = "";
	private String fileLocationTempDirectory = "";

	private Map<String, Object> rJsonMap = new HashMap<String, Object>();

	private String[] fileType = new String[]{".jpg",".gif",".bmp",".png", ".jpeg"};

	public Upload (String fileMax, String domainUrl, String fileLocationTempDirectory)
	{
		this.fileMax = fileMax;
		this.domainUrl = domainUrl.trim();
		this.fileLocationTempDirectory = fileLocationTempDirectory;
	}

	/*
	 * 获取返回数据
	 */
	public String getStringValue(String field) {
		String rStr = "";
		Object o = rJsonMap.get(field);
		if(o != null && !"".equals(o))
		{
			rStr = o.toString();
		}
		return rStr;
	}

	/*
	 * 跨域请求
	 * @return
	 */
	public String requestDomainUpload (HttpServletRequest request, String path) throws DomainFileUploadException
	{
        Map<String, String> textMap = new HashMap<String, String>();
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("file", path);
		String str = formUpload(domainUrl, textMap, fileMap);
		if(str != null && !"".equals(str))
		{
			rJsonMap = StringUtil.JsonToMap(JSONObject.fromObject(str));
		}
		return str;
	}


	/*
     * 上传图片
     */
    private String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) throws DomainFileUploadException {
        String res = "";
        HttpURLConnection conn = null;
        String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // text
            if (textMap != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                out.write(strBuf.toString().getBytes());
            }

            // file
            if (fileMap != null) {
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = (String) entry.getKey();
                    String inputValue = (String) entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    File file = new File(inputValue);
                    String filename = file.getName();
                    MagicMatch match = Magic.getMagicMatch(file, false, true);
                    String contentType = match.getMimeType();

                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                    out.write(strBuf.toString().getBytes());

                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            out.write(endData);
            out.flush();
            out.close();

            // 读取返回数据
            StringBuffer strBuf = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                strBuf.append(line).append("\n");
            }
            res = strBuf.toString();
            reader.close();
            reader = null;
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.info("发送POST请求出错。" + urlStr);
            throw new DomainFileUploadException();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return res;
    }

    /*
     * 获取上传文件目录
     */
    public String getUploadFileDirectory() {
    	return ContextLoader.getCurrentWebApplicationContext().getServletContext().getRealPath(fileLocationTempDirectory) + "/";
    }

    /*
     * 上传文件到本地临时目录
     */
	@SuppressWarnings("unchecked")
	public Map<String, String> fileLocationTempUpload(HttpServletRequest request, HttpServletResponse response) {

        String relaPath = getUploadFileDirectory();

        String temp = relaPath + "temp";

        //创建该目录
        createDirectory(relaPath);
        createDirectory(temp);

        //配置
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(20 * 1024);
        factory.setRepository(new File(temp));

        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(Integer.valueOf(20) * 1024 * 1024);
        upload.setFileSizeMax(Integer.valueOf(20) * 1024 * 1024);

        //处理请求中的文件
        List<FileItem> items = null;
        try {
            items = upload.parseRequest(request);
        } catch (FileUploadException e) {
           // e.printStackTrace();
        }
        String filePath = "";
        String urlPath = "";
        String fName = "";
        for (FileItem item : items)
        {
        	if(!item.isFormField())
        	{
        		String fileName = item.getName().toLowerCase();
                if(validateFileFmt(fileName)){

            		String dsign = SignUtils.generateDateSign("yyyyMMddHHmmss");
            		String random4 = SignUtils.generateRandom(4);

            		fName = dsign + random4 + fileName.substring(fileName.lastIndexOf("."));

                    filePath = relaPath + fName;
                    urlPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort() + request.getContextPath() + "/upload/" + fName;
                    try {
                        item.write(new File(filePath));
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        throw new Exception();
                    } catch (Exception e) {
                       // e.printStackTrace();
                    }
                }
        	}
        	else
        	{
        		String formField = item.getFieldName();
        		if("tempFileName".equals(formField))
        		{
        			//删除临时文件
        			removeLocationUploadFile(relaPath + item.getString());
        		}
        	}
        }

        Map<String, String> path = new HashMap<String, String>();
        path.put("filePath", filePath);
        path.put("urlPath", urlPath);
        path.put("fileName", fName);

		return path;
	}

	/*
	 * 目录不存在创建目录
	 */
	private void createDirectory(String directory) {
		if(!new File(directory).isDirectory())
		{
			new File(directory).mkdirs();
		}
	}

	/*
	 * 校验上传文件格式
	 */
	private boolean validateFileFmt(String fileName) {
		for(String ftype : fileType)
		{
			if(fileName.endsWith(ftype))
			{
				return true;
			}
		}
		return false;
	}

	/*
	 * 删除本地上传文件
	 */
	public void removeLocationUploadFile(String filePath) {
		if(filePath != null && !"".equals(filePath))
		{
			File file = new File(filePath);
			if(file.exists())
			{
				if(file.isFile())
				{
					file.delete();
				}
			}
		}
	}

}
