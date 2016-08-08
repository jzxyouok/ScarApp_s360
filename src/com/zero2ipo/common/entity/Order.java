package com.zero2ipo.common.entity;

public class Order {
	private String orderId;
	private String userId;
	private String userName;
	private String goodsId;
	private String cardsId;
	private String carNum;
	private String carType;
	private String washType;
	private String address;
	private String lat;
	private String lon;
	private float price;
	private String discription;
	private String startPhoto;
	private String endPhoto;
	private String createTime;
	private String payTime;
	private String confirmTime;
	private String washTime;
	private String finishTime;
	private String comment;
	private String payType;
	private String orderStatus;
	private String confirmInfo;
	private String sendOrderToName;
	private String sendOrderStatus;
	private String mobile;
	private String carColor;
	private String jsParam;
	private int id;
	private String status;
	private String carId;
	private String transactionId;//微信支付成功之后返回商户订单号
	private String outTradeNo;
    private float qianbao;//钱包付款金额
    private String vipCouponId;//如果使用优惠券抵扣了，那么vipCouponId就有值了

	public String getVipCouponId() {
		return vipCouponId;
	}

	public void setVipCouponId(String vipCouponId) {
		this.vipCouponId = vipCouponId;
	}

	public float getQianbao() {
		return qianbao;
	}

	public void setQianbao(float qianbao) {
		this.qianbao = qianbao;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getStatus() {
		return orderStatus;
	}

	public void setStatus(String status) {
		this.status = this.orderStatus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getSendOrderStatus() {
		return sendOrderStatus;
	}

	public void setSendOrderStatus(String sendOrderStatus) {
		this.sendOrderStatus = sendOrderStatus;
	}

	public String getSendOrderToName() {
		return sendOrderToName;
	}

	public void setSendOrderToName(String sendOrderToName) {
		this.sendOrderToName = sendOrderToName;
	}

	public void setOrderId(String orderId){
		this.orderId=orderId;
	}
	public String getOrderId(){
		return orderId;
	}
	public void setUserId(String userId){
		this.userId=userId;
	}
	public String getUserId(){
		return userId;
	}
	public void setGoodsId(String goodsId){
		this.goodsId=goodsId;
	}
	public String getGoodsId(){
		return goodsId;
	}
	public void setCarNum(String carNum){
		this.carNum=carNum;
	}
	public String getCarNum(){
		return carNum;
	}
	public void setCarType(String carType){
		this.carType=carType;
	}
	public String getCarType(){
		return carType;
	}
	public void setWashType(String washType){
		this.washType=washType;
	}
	public String getWashType(){
		return washType;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setLat(String lat){
		this.lat=lat;
	}
	public String getLat(){
		return lat;
	}
	public void setLon(String lon){
		this.lon=lon;
	}
	public String getLon(){
		return lon;
	}
	public void setPrice(float price){
		this.price=price;
	}
	public float getPrice(){
		return price;
	}
	public void setDiscription(String discription){
		this.discription=discription;
	}
	public String getDiscription(){
		return discription;
	}
	public void setStartPhoto(String startPhoto){
		this.startPhoto=startPhoto;
	}
	public String getStartPhoto(){
		return startPhoto;
	}
	public void setEndPhoto(String endPhoto){
		this.endPhoto=endPhoto;
	}
	public String getEndPhoto(){
		return endPhoto;
	}
	public void setCreateTime(String createTime){
		this.createTime=createTime;
	}
	public String getCreateTime(){
		return createTime;
	}
	public void setPayTime(String payTime){
		this.payTime=payTime;
	}
	public String getPayTime(){
		return payTime;
	}
	public void setConfirmTime(String confirmTime){
		this.confirmTime=confirmTime;
	}
	public String getConfirmTime(){
		return confirmTime;
	}
	public void setWashTime(String washTime){
		this.washTime=washTime;
	}
	public String getWashTime(){
		return washTime;
	}
	public void setFinishTime(String finishTime){
		this.finishTime=finishTime;
	}
	public String getFinishTime(){
		return finishTime;
	}
	public void setComment(String comment){
		this.comment=comment;
	}
	public String getComment(){
		return comment;
	}
	public void setPayType(String payType){
		this.payType=payType;
	}
	public String getPayType(){
		return payType;
	}
	public void setOrderStatus(String orderStatus){
		this.orderStatus=orderStatus;
	}
	public String getOrderStatus(){
		return orderStatus;
	}
	public void setConfirmInfo(String confirmInfo){
		this.confirmInfo=confirmInfo;
	}
	public String getConfirmInfo(){
		return confirmInfo;
	}

	public String getJsParam() {
		return jsParam;
	}

	public void setJsParam(String jsParam) {
		this.jsParam = jsParam;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getCardsId() {
		return cardsId;
	}

	public void setCardsId(String cardsId) {
		this.cardsId = cardsId;
	}
}
