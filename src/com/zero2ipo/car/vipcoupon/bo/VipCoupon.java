package com.zero2ipo.car.vipcoupon.bo;

import java.util.Date;

/**
 * vipCoupon
 * Wed Dec 02 15:55:28 GMT+08:00 2015
 */


public class VipCoupon implements  java.io.Serializable{
	private long id;
	private String userId;
	private String couponCode;
	private Date createTime;
	private String couponName;
	private String couponMoney;
	private String couponStartTime;
	private String couponEndTime;
	private String couponRemark;
	private String status;

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setId(long id){
		this.id=id;
	}
	public long getId(){
		return id;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setCouponCode(String couponCode){
		this.couponCode=couponCode;
	}
	public String getCouponCode(){
		return couponCode;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
	public void setCouponName(String couponName){
		this.couponName=couponName;
	}
	public String getCouponName(){
		return couponName;
	}
	public void setCouponMoney(String couponMoney){
		this.couponMoney=couponMoney;
	}
	public String getCouponMoney(){
		return couponMoney;
	}
	public void setCouponStartTime(String couponStartTime){
		this.couponStartTime=couponStartTime;
	}
	public String getCouponStartTime(){
		return couponStartTime;
	}
	public void setCouponEndTime(String couponEndTime){
		this.couponEndTime=couponEndTime;
	}
	public String getCouponEndTime(){
		return couponEndTime;
	}
	public void setCouponRemark(String couponRemark){
		this.couponRemark=couponRemark;
	}
	public String getCouponRemark(){
		return couponRemark;
	}

}

