package com.zero2ipo.mobile.services.user;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.module.entity.user.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 *
 * 用户服务接口
 *
 */

@Service
@Transactional
public interface IUserServices {

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
	 * 根据手机号和密码查找用户
	 * @param mobile
	 * @return
	 */
	public List<UserEntity> findUserByMobileAndPassword(String mobile, String password);

	/**
	 * 保存用户基本信息
	 *
	 * @param openId
	 * @param mobile
	 * @param password
	 * @param userIrId
	 * @return
	 */
	public Users saveUserCore(String openId, String mobile, String password, String userIrId);
	/**
	 * 更新用户微信openid
	 * @param user
	 */
	public void updateUser(Users user);

	/**
	 * 更新个人用户信息
	 * @param user
	 */
	public void updateUserQianBao(Users user);

	/**
	 * 跟新机构用户信息
	 */
	public UserEntity updateOrgUserInfo(UserEntity user);

	/**
	 * CRM用户更新
	 */
	public UserEntity updateCrmUserInfo(UserEntity user);

	/**
	 * 修改用户密码
	 */
	public void updateUserPassword(Users user);

	/**
	 * 通过邮箱查找用户
	 * @param email
	 * @return
	 */
	public List<UserEntity> findUserByEmail(String email);

	/**
	 * 跟新用户登录次数
	 */
	public void updateUserLoginNum(UserEntity user);

	/**
	 * 根据用户ID查询会员信息
	 * @param userId
	 */
	public Users findUserByUserId(String userId);

	public boolean updateMobile(Users user);

	public Users findUserByMap(Map<String, Object> queryMap);
	/**
	 * 洗车工登陆
	 * @param queryMap
	 * @return
	 */
	public AdminBo findAdminLoginMessage(Map<String, Object> queryMap);
	/**
	 * 洗车工任务更新
	 */
	public boolean updateSendOrder(SendOrder sendOrder);

	/**
	 * 为洗车工绑定微信账号
	 * @param admin
	 * @return
	 */
	public boolean updateAdmin(AdminBo admin);

	public AdminBo findAdminByLatLng(String lat, String lng);

	public void reduceQianBao(Users user);

	public List<AdminBo> findAdminList();

	int findUserByMapCount(Map<String, Object> map);

	void updateUserQianBaoByOpenId(Users u);
}
