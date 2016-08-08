package com.zero2ipo.common.upload;

import com.zero2ipo.framework.util.Config;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Servlet implementation class UploadAction
 */
@Controller
public class UploadAction {
	private static final long serialVersionUID = 1L;

	//上传文件的保存路径
    protected String configPath = "upload/";

    protected String dirTemp = "upload/temp";

    protected String dirName = "file";

    protected String hdUrl="/";

    @RequestMapping(value = "/html5/upload.html", method = RequestMethod.POST)
    @ResponseBody
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        //文件保存目录路径
        Config config=new Config();
        String localPath=config.getString("downimage");
         String compon= localPath;
         String savePath = compon;

        // 临时文件目录
        String tempPath =savePath+ dirTemp;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String ymd = sdf.format(new Date());
        String imageDate="/image/" + ymd + "/";
        savePath += imageDate;
        //回调路径
       // hdUrl+=configPath + ymd + "/";
        //创建文件夹
        File dirFile = new File(savePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        tempPath += "/" + ymd + "/";
        //创建临时文件夹
        File dirTempFile = new File(tempPath);
        if (!dirTempFile.exists()) {
            dirTempFile.mkdirs();
        }

        DiskFileItemFactory  factory = new DiskFileItemFactory();
        factory.setSizeThreshold(20 * 1024 * 1024); //设定使用内存超过5M时，将产生临时文件并存储于临时目录中。
        factory.setRepository(new File(tempPath)); //设定存储临时文件的目录。
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        try {
            List items = upload.parseRequest(request);
            Iterator itr = items.iterator();
            while (itr.hasNext())
            {
                FileItem item = (FileItem) itr.next();
                String fileName = item.getName();
                long fileSize = item.getSize();
                if (!item.isFormField()) {
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
                    try{
                        File uploadedFile = new File(savePath, newFileName);
                        //第二种方法
                        OutputStream os = new FileOutputStream(uploadedFile);
                        InputStream is = item.getInputStream();
                        byte buf[] = new byte[1024];//可以修改 1024 以提高读取速度
                        int length = 0;
                        while( (length = is.read(buf)) > 0 ){
                            os.write(buf, 0, length);
                        }
                        //将is转换为base64格式图片
                        //String base64Image=GetImage.getBase64Image(is);
                        //关闭流
                        os.flush();
                        os.close();
                        is.close();
                        System.out.println("上传成功！路径："+savePath+"/"+newFileName);
                        out.print(imageDate+newFileName);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else {
                    String filedName = item.getFieldName();
                    System.out.println("FieldName："+filedName);
                    System.out.println("String："+item.getString());
                }
            }

        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.flush();
        out.close();

	}

}
