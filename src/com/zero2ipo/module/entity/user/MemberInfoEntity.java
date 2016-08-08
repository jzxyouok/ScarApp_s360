package com.zero2ipo.module.entity.user;
import java.util.Date;

   /**
    * memberInfo 实体类
    * Mon Feb 02 16:00:01 GMT+08:00 2015 zhengyunfei
    */ 


public class MemberInfoEntity{
	private int memberId;
	private String institutionsId;
	private String memberRealname;
	private String sex;
	private String mobile;
	private String tel;
	private String email;
	private String fax;
	private String address;
	private String interests;
	private String introduce;
	private String note;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	public void setMemberId(int memberId){
	this.memberId=memberId;
	}
	public int getMemberId(){
		return memberId;
	}
	public void setInstitutionsId(String institutionsId){
	this.institutionsId=institutionsId;
	}
	public String getInstitutionsId(){
		return institutionsId;
	}
	public void setMemberRealname(String memberRealname){
	this.memberRealname=memberRealname;
	}
	public String getMemberRealname(){
		return memberRealname;
	}
	public void setSex(String sex){
	this.sex=sex;
	}
	public String getSex(){
		return sex;
	}
	public void setMobile(String mobile){
	this.mobile=mobile;
	}
	public String getMobile(){
		return mobile;
	}
	public void setTel(String tel){
	this.tel=tel;
	}
	public String getTel(){
		return tel;
	}
	public void setEmail(String email){
	this.email=email;
	}
	public String getEmail(){
		return email;
	}
	public void setFax(String fax){
	this.fax=fax;
	}
	public String getFax(){
		return fax;
	}
	public void setAddress(String address){
	this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setInterests(String interests){
	this.interests=interests;
	}
	public String getInterests(){
		return interests;
	}
	public void setIntroduce(String introduce){
	this.introduce=introduce;
	}
	public String getIntroduce(){
		return introduce;
	}
	public void setNote(String note){
	this.note=note;
	}
	public String getNote(){
		return note;
	}
	public void setCreateUser(String createUser){
	this.createUser=createUser;
	}
	public String getCreateUser(){
		return createUser;
	}
	public void setCreateTime(Date createTime){
	this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setUpdateUser(String updateUser){
	this.updateUser=updateUser;
	}
	public String getUpdateUser(){
		return updateUser;
	}
	public void setUpdateTime(Date updateTime){
	this.updateTime=updateTime;
	}
	public Date getUpdateTime(){
		return updateTime;
	}
	
}

