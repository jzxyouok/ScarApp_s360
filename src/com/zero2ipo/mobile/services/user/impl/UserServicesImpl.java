package com.zero2ipo.mobile.services.user.impl;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.core.md5.MD5PwdEncoder;
import com.zero2ipo.mobile.dao.user.IUserDao;
import com.zero2ipo.mobile.services.user.IUserServices;
import com.zero2ipo.module.entity.user.UserEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 用户服务层接口实现
 * @author zhengyunfei
 *
 */

@Component("userServices")
	public class UserServicesImpl implements IUserServices{

	/**
	 * 根据用户名密码查找用户
	 */
	public Users findUserByUsernameAndPassword(String username, String password) {
		Users u=null;
		try{
			u=userDao.findUserByUsernameAndPassword(username, MD5PwdEncoder.generatePassword(password));
		}catch (Exception e){
			e.printStackTrace();
		}
		return u;
	}

	/**
	 * 根据手机号查找用户
	 * @param mobile
	 * @return
	 */
	public List<Users> findUserByMobile(String mobile) {

		return userDao.findUserByMobile(mobile);
	}

	/**
	 * 根据手机号和密码查找用户
	 * @param mobile
	 * @return
	 */
	public List<UserEntity> findUserByMobileAndPassword(String mobile, String password) {

		return userDao.findUserByMobileAndPassword(mobile, MD5PwdEncoder.generatePassword(password));
	}
	/**
	 * 根据用户ID查询会员信息
	 * @param userId
	 */
	public Users findUserByUserId(String userId){
		return userDao.findUserById(userId);
	}
	/**
	 * 保存用户基本信息
	 *
	 * @param openId
	 * @param mobile
	 * @param password
	 * @return
	 */
	public Users saveUserCore(String openId, String mobile, String password, String userIrId) {
		//基础信息
		Users user = new Users();
		user.setUserId(mobile);
		//user.setAccount(userIrId);
		user.setPhoneNum(mobile);
		user.setPassword(MD5PwdEncoder.generatePassword(password));
		//user.setUserRegisterStep(1);
		long id=userDao.saveUser(user);
		Map<String,Object> queryMap=new HashMap<String, Object>();
		queryMap.put("mobile",mobile);
		user.setId(id);
		return user;

	}

	/**
	 * 更新个人用户信息
	 * @param user
	 */
	public Users updateUserInfo(Users user) {

		Users u = userDao.findUserById(user.getUserId());

		return user;
	}
	/**
	 * 更新用户微信openid
	 * @param user
	 */
	public void updateUser(Users user){
		if(user!=null){
			userDao.updateUser(user);
		}
	}

	/**
	 * 更新个人用户信息
	 *
	 * @param user
	 */
	@Override
	public void updateUserQianBao(Users user) {
		userDao.updateUserQianBao(user);
	}
	public void reduceQianBao(Users user){
		userDao.reduceQianBao(user);
	}

	@Override
	public List<AdminBo> findAdminList() {
		return userDao.findAdminList();
	}

	@Override
	public int findUserByMapCount(Map<String, Object> map) {
		return userDao.findUserByMapCount(map);
	}

	@Override
	public void updateUserQianBaoByOpenId(Users u) {
		userDao.updateUserQianBaoByOpenId(u);
	}

	/**
	 * 跟新机构用户信息
	 */
	public UserEntity updateOrgUserInfo(UserEntity user) {
		Users u = userDao.findUserById(user.getUserId());
		if(u != null)
		{
			userDao.updateOrgUserInfo(user);
		}
		return user;
	}

	/**
	 * CRM用户更新
	 */
	public UserEntity updateCrmUserInfo(UserEntity user) {
		Users u = userDao.findUserById(user.getUserId());
		if(u != null)
		{
			userDao.updateCrmUserInfo(user);
		}
		return user;
	}

	/**
	 * 修改用户密码
	 */
	public void updateUserPassword(Users user){

		userDao.updateUserPassword(user);
	}

	/**
	 * 跟新用户登录次数
	 */
	public void updateUserLoginNum(UserEntity user) {
		userDao.updateUserLoginNum(user);
	}

	/**
	 * 通过邮箱查找用户
	 * @param email
	 * @return
	 */
	public List<UserEntity> findUserByEmail(String email) {
		return userDao.findUserByEmail(email);
	}

	public boolean updateMobile(Users mobile) {
		// TODO Auto-generated method stub
		return userDao.updateMobile(mobile);
	}
	/*
	 * 用户DAO接口注入
	 */
	@Resource(name = "userDao")
	private IUserDao userDao;
	@Override
	public Users findUserByMap(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return userDao.findUserByMap(queryMap);
	}

	@Override
	public AdminBo findAdminLoginMessage(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return userDao.findAdminLoginMessage(queryMap);
	}

	@Override
	public boolean updateSendOrder(SendOrder sendOrder){
		 return userDao.updateSendOrder(sendOrder);

	}

	@Override
	public boolean updateAdmin(AdminBo admin) {
		// TODO Auto-generated method stub
		return userDao.updateAdmin(admin);
	}

	@Override
	public AdminBo findAdminByLatLng(String lat, String lng) {
		return userDao.findAdminByLatLng(lat,lng);
	}


}
