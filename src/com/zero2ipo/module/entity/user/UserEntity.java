package com.zero2ipo.module.entity.user;

import com.zero2ipo.common.GlobalConstant;

/**
 * cfjUser 实体类 Fri Sep 12 09:54:55 GMT+08:00 2014 郑云飞
 */

public class UserEntity {

	private String userId;

	private String userName;

	private String userPassword;

	private String mobile;

	private String userStatus;

	private String userGroup;

	private String userInputtime;

	private int userLoginnum;

	private String userLasttime;

	private String remark;

	private String userType;

	private int userRegisterStep;
	
	private String userHeadPortrait;

	private UserInfoEntity userInfo;

	private OrganizationUserInfoEntity orgUserInfo;

	private UserCrmInfoEntity userCrmInfo;
	
	private String userIrId;
	private String openId;
    private String areaName;
	private String jrMoney;
	private String source;
	private String shareUserId;

	public String getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getJrMoney() {
		return jrMoney;
	}

	public void setJrMoney(String jrMoney) {
		this.jrMoney = jrMoney;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
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

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public UserInfoEntity getUserInfo() {
		return userInfo;
	}

	public String getUserInputtime() {
		return userInputtime;
	}

	public void setUserInputtime(String userInputtime) {
		this.userInputtime = userInputtime;
	}

	public int getUserLoginnum() {
		return userLoginnum;
	}

	public void setUserLoginnum(int userLoginnum) {
		this.userLoginnum = userLoginnum;
	}

	public String getUserLasttime() {
		return userLasttime;
	}

	public void setUserLasttime(String userLasttime) {
		this.userLasttime = userLasttime;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public int getUserRegisterStep() {
		return userRegisterStep;
	}

	public String getUserHeadPortrait() {
		return userHeadPortrait;
	}

	public void setUserHeadPortrait(String userHeadPortrait) {
		this.userHeadPortrait = userHeadPortrait;
	}

	public void setUserRegisterStep(int userRegisterStep) {
		this.userRegisterStep = userRegisterStep;
	}

	public void setUserInfo(UserInfoEntity userInfo) {
		if (userInfo != null && userInfo.getUserInfoId() != null) {
			this.setUserType(GlobalConstant.USER_TYPE);
		}
		this.userInfo = userInfo;
	}

	public OrganizationUserInfoEntity getOrgUserInfo() {
		return orgUserInfo;
	}

	public void setOrgUserInfo(OrganizationUserInfoEntity orgUserInfo) {
		if (orgUserInfo != null && orgUserInfo.getOrgUserInfoId() != null) {
			this.setUserType(GlobalConstant.ORG_USER_TYPE);
		}
		this.orgUserInfo = orgUserInfo;
	}

	public UserCrmInfoEntity getUserCrmInfo() {
		return userCrmInfo;
	}

	public void setUserCrmInfo(UserCrmInfoEntity userCrmInfo) {
		if (userCrmInfo != null && userCrmInfo.getUserCrmId() != null) {
			this.setUserType(GlobalConstant.CRM_USER_TYPE);
		}
		this.userCrmInfo = userCrmInfo;
	}

	public String getUserIrId() {
		return userIrId;
	}

	public void setUserIrId(String userIrId) {
		this.userIrId = userIrId;
	}

}
