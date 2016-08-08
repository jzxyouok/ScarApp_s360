package com.zero2ipo.module.entity.user;

/**
 * 用户扩展信息表
 * @author liyang
 * @date 2014-10-10
 */
public class UserInfoEntity {
	
	private String userInfoId;

	private String userId;

	private String userName;

	private String sex;

	private String maile;

	private String position;

	private String company;

	private String likePro;

	private String column9;
	
	private String userCardUrl;
	
	private String idCardUrl;
	
	private String idCard;
	private String remark;
	private String invitationCode;
	private String assest;
	private String arpm;
	private String codeArea;
	private String userPhoto;//用户头像
	
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getCodeArea() {
		return codeArea;
	}
	public void setCodeArea(String codeArea) {
		this.codeArea = codeArea;
	}
	public String getAssest() {
		return assest;
	}
	public void setAssest(String assest) {
		this.assest = assest;
	}
	public String getArpm() {
		return arpm;
	}
	public void setArpm(String arpm) {
		this.arpm = arpm;
	}
	
	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getUserInfoId() {
		return userInfoId;
	}

	public void setUserInfoId(String userInfoId) {
		this.userInfoId = userInfoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getMaile() {
		return maile;
	}

	public void setMaile(String maile) {
		this.maile = maile;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLikePro() {
		return likePro;
	}

	public void setLikePro(String likePro) {
		this.likePro = likePro;
	}

	public String getColumn9() {
		return column9;
	}

	public void setColumn9(String column9) {
		this.column9 = column9;
	}

	public String getUserCardUrl() {
		return userCardUrl;
	}

	public void setUserCardUrl(String userCardUrl) {
		this.userCardUrl = userCardUrl;
	}

	public String getIdCardUrl() {
		return idCardUrl;
	}

	public void setIdCardUrl(String idCardUrl) {
		this.idCardUrl = idCardUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
