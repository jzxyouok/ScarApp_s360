package com.zero2ipo.common.entity;

public class Address {
	private String washAddr;
	private String mobile;
	private int cid;
	private int id;
	private String openId;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWashAddr() {
		return washAddr;
	}
	public void setWashAddr(String washAddr) {
		this.washAddr = washAddr;
	}
	public String getMobile() {
		return mobile==null?"":mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	
}
