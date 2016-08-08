package com.zero2ipo.module.entity.picture;

   /**
    * cfjPicture 实体类
    * Mon Oct 13 11:44:45 GMT+08:00 2014 郑云飞
    */ 


public class PictureEntity{
	private String pid;
	private String id;
	private String name;
	private String url;
	private String attachmentUrl;
	private String tum;
	private String remark;
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName(){
		return name;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public void setUrl(String url){
	this.url=url;
	}
	public String getUrl(){
		return url;
	}

	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	
	public String getTum() {
		return tum;
	}
	public void setTum(String tum) {
		this.tum = tum;
	}
	public void setRemark(String remark){
	this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
	
}

