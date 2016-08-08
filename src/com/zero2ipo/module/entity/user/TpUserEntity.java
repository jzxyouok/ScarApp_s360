package com.zero2ipo.module.entity.user;

/**
 * 第三方用户与财富街用户绑定表 liyang
 */

public class TpUserEntity {

	private int id; 					// ID

	private String userId;				// 财富街用户ID

	private String tpUserId; 			// 第三方用户ID

	private String tpUserName; 		// 第三方用户名称

	private String tpUserInputtime; 	// 绑定时间

	private String tpSource; 			// 第三方用户来源

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTpUserId() {
		return tpUserId;
	}

	public void setTpUserId(String tpUserId) {
		this.tpUserId = tpUserId;
	}

	public String getTpUserName() {
		return tpUserName;
	}

	public void setTpUserName(String tpUserName) {
		this.tpUserName = tpUserName;
	}

	public String getTpUserInputtime() {
		return tpUserInputtime;
	}

	public void setTpUserInputtime(String tpUserInputtime) {
		this.tpUserInputtime = tpUserInputtime;
	}

	public String getTpSource() {
		return tpSource;
	}

	public void setTpSource(String tpSource) {
		this.tpSource = tpSource;
	}

}
