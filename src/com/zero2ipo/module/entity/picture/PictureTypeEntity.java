package com.zero2ipo.module.entity.picture;


   /**
    * cfjPicture 实体类
    * Mon Oct 13 11:44:45 GMT+08:00 2014 郑云飞
    */ 


public class PictureTypeEntity{
	private String id;
	private String name;
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
	
	public void setRemark(String remark){
	this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
}

