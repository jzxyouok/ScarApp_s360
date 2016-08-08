package com.zero2ipo.common.entity;

/**
 * Created by Administrator on 2016/8/4.
 */
public class OrderTongJiResult implements java.io.Serializable{
    private String sumMoney;
    private String sumTai;
    private String startDate;
    private String endDate;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(String sumMoney) {
        this.sumMoney = sumMoney;
    }

    public String getSumTai() {
        return sumTai;
    }

    public void setSumTai(String sumTai) {
        this.sumTai = sumTai;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
