package com.zero2ipo.module.entity.user;

/**
 * 机构用户扩展信息
 * @author liyang
 *
 */
public class OrganizationUserInfoEntity {
	
	private String orgUserInfoId;

	private String userId;

	private String orgName;

	private String orgAssets;

	private String orgDutypersonName;

	private String orgDutypersonSex;

	private String orgDutypepsonMobile;

	private String orgDutypepsonEmaile;

	private String orgDutypersonPosition;

	private String orgDutypersonCardurl;

	private String orgLicenseUrl;

	private String likeProducts;
	
	private String invitationCode;
	private String codeArea;
	private String remark;
	
	public String getRemark() {
		return remark==null?"":remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCodeArea() {
		return codeArea;
	}

	public void setCodeArea(String codeArea) {
		this.codeArea = codeArea;
	}

	public String getOrgUserInfoId() {
		return orgUserInfoId;
	}

	public void setOrgUserInfoId(String orgUserInfoId) {
		this.orgUserInfoId = orgUserInfoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrgAssets() {
		return orgAssets;
	}

	public void setOrgAssets(String orgAssets) {
		this.orgAssets = orgAssets;
	}

	public String getOrgDutypersonName() {
		return orgDutypersonName;
	}

	public void setOrgDutypersonName(String orgDutypersonName) {
		this.orgDutypersonName = orgDutypersonName;
	}

	public String getOrgDutypersonSex() {
		return orgDutypersonSex;
	}

	public void setOrgDutypersonSex(String orgDutypersonSex) {
		this.orgDutypersonSex = orgDutypersonSex;
	}

	public String getOrgDutypepsonMobile() {
		return orgDutypepsonMobile;
	}

	public void setOrgDutypepsonMobile(String orgDutypepsonMobile) {
		this.orgDutypepsonMobile = orgDutypepsonMobile;
	}

	public String getOrgDutypepsonEmaile() {
		return orgDutypepsonEmaile;
	}

	public void setOrgDutypepsonEmaile(String orgDutypepsonEmaile) {
		this.orgDutypepsonEmaile = orgDutypepsonEmaile;
	}

	public String getOrgDutypersonPosition() {
		return orgDutypersonPosition;
	}

	public void setOrgDutypersonPosition(String orgDutypersonPosition) {
		this.orgDutypersonPosition = orgDutypersonPosition;
	}

	public String getOrgDutypersonCardurl() {
		return orgDutypersonCardurl;
	}

	public void setOrgDutypersonCardurl(String orgDutypersonCardurl) {
		this.orgDutypersonCardurl = orgDutypersonCardurl;
	}

	public String getOrgLicenseUrl() {
		return orgLicenseUrl;
	}

	public void setOrgLicenseUrl(String orgLicenseUrl) {
		this.orgLicenseUrl = orgLicenseUrl;
	}

	public String getLikeProducts() {
		return likeProducts;
	}

	public void setLikeProducts(String likeProducts) {
		this.likeProducts = likeProducts;
	}

	public String getInvitationCode() {
		return invitationCode;
	}

	public void setInvitationCode(String invitationCode) {
		this.invitationCode = invitationCode;
	}
	
}
