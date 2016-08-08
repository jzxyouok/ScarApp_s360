package com.zero2ipo.module.entity.user;

/**
 * 
 * CRM系统与CMS系统关联用户ID表实体对象
 * @author liyang
 *
 */
public class UserCrmInfoEntity {

	private String userCrmId;
	
	private String userId;
	
	private String crmId;

	public String getUserCrmId() {
		return userCrmId;
	}

	public void setUserCrmId(String userCrmId) {
		this.userCrmId = userCrmId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String crmId) {
		this.crmId = crmId;
	}
	
}
