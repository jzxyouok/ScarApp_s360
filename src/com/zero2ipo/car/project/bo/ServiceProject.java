package com.zero2ipo.car.project.bo;
public class ServiceProject{
	private String id;
	private String name;
	private String price;
	private float orignPrice;
	private String remark;
	private String isMain;
	private String washCount;//洗满几次后价格调整为
	private String discountPrice;//折扣价格
	private float firstPrice;//首次洗车价格

	public float getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(float firstPrice) {
		this.firstPrice = firstPrice;
	}

	public String getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(String discountPrice) {
		this.discountPrice = discountPrice;
	}

	public String getWashCount() {
		return washCount;
	}

	public void setWashCount(String washCount) {
		this.washCount = washCount;
	}

	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public void setId(String id){
	this.id=id;
	}
	public String getId(){
		return id;
	}
	public void setName(String name){
	this.name=name;
	}
	public String getName()
	{
		return name.trim();
	}
	public void setPrice(String price){
	this.price=price;
	}
	public String getPrice(){
		return price;
	}
	public void setOrignPrice(float orignPrice){
	this.orignPrice=orignPrice;
	}
	public float getOrignPrice(){
		return orignPrice;
	}
	public void setRemark(String remark){
	this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}

}

