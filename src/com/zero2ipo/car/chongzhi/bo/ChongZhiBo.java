package com.zero2ipo.car.chongzhi.bo;
public class ChongZhiBo implements java.io.Serializable{
	private String id;
	private float money;
	private float zsmoney;
	private String expDate;
	private String status;
	private String remark;
	private String totalMoney;

	public String getTotalMoney() {
		float t=money+zsmoney;
		return t+"";
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getMoney() {
		return money;
	}

	public void setMoney(float money) {
		this.money = money;
	}

	public float getZsmoney() {
		return zsmoney;
	}

	public void setZsmoney(float zsmoney) {
		this.zsmoney = zsmoney;
	}

	public String getExpDate() {
		return expDate.replace(".0","");
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

