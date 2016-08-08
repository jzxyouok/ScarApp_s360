package com.zero2ipo.mobile.dao.user;

import java.util.List;
import java.util.Map;

import com.zero2ipo.common.entity.app.Users;
import org.springframework.stereotype.Repository;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.module.entity.user.UserEntity;

/**
 * 用户dao接口
 * @author zhengyunfei
 *
 */
@Repository
public interface IUserDao {

	/**
	 * 根据用户名密码查找用户
	 * @param username
	 * @param password
	 * @return
	 */
	public Users findUserByUsernameAndPassword(String username, String password);

	/**
	 * 根据手机号查找用户
	 * @param mobile
	 * @return
	 */
	public List<Users> findUserByMobile(String mobile);

	/**
	 * 注册用户基本信息
	 * @param userType
	 * @param mobile
	 * @param password
	 * @return
	 */
	public long saveUser(Users user);

	/**
	 * 根据用户ID查找用户
	 * @param uuid
	 * @return
	 */
	public Users findUserById(String uuid);

	/**
	 * 注册个人用户信息
	 * @param userType
	 * @param mobile
	 * @param password
	 * @return
	 */
	public void saveUserInfo(UserEntity user);


	public void updateUserQianBao(Users user);

	/**
	 * 更新个人用户信息
	 * @param user
	 */
	public void updateUserInfo(Users user);

	/**
	 * 更新机构用户信息
	 */
	public void updateOrgUserInfo(UserEntity user);

	/**
	 * 更新CRM用户信息
	 */
	public void updateCrmUserInfo(UserEntity user);

	/**
	 * 修改用户密码
	 */
	public void updateUserPassword(Users user);

	/**
	 * 根据邮箱查找邮箱
	 * @param email
	 * @return
	 */
	public List<UserEntity> findUserByEmail(String email);

	/**
	 * 更具手机好和密码查找用户
	 * @param mobile
	 * @param password
	 * @return
	 */
	public List<UserEntity> findUserByMobileAndPassword(String mobile,
														String password);

	/**
	 * 跟新用户登录次数
	 */
	public void updateUserLoginNum(UserEntity user);
	/**
	 * 跟新用户信息
	 */
	public void updateUser(Users u);

	public boolean  updateMobile(Users mobile);

	public Users findUserByMap(Map<String, Object> queryMap);

	public AdminBo findAdminLoginMessage(Map<String, Object> queryMap);

	public boolean updateSendOrder(SendOrder sendOrder);

	public boolean updateAdmin(AdminBo admin);

	public AdminBo findAdminByLatLng(String lat, String lng);

	public void reduceQianBao(Users user);

	public List<AdminBo> findAdminList();

	int findUserByMapCount(Map<String, Object> map);

	void updateUserQianBaoByOpenId(Users u);
}
