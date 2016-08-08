package com.zero2ipo.mobile.dao.user.impl;

import com.zero2ipo.common.entity.AdminBo;
import com.zero2ipo.common.entity.CodeCommon;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.core.baiduMap.DistanceUtil;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.user.IUserDao;
import com.zero2ipo.module.entity.user.UserEntity;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户Dao实现类
 * @author zhengyunfei
 *
 */

@Component("userDao")
public class UserDaoImpl extends IbatisBaseDao implements IUserDao{

	//根据用户名、密码查找用户
	private static final String FIND_USER_BY_USERNAME_PASSWORD = "com.app.mobile.user.findUsersByMap";
	//根据手机号查找用户
	private static final String FIND_USERS_BY_MOBILE = "com.app.mobile.user.findUsersList";
	//根据手机号和密码查找
	private static final String FIND_USERS_BY_MOBILE_PWD = "zero2ipo.mobile.user.findUserByMobileAndPwd";
	//根据ID查找用户
	private static final String FIND_USER_BY_ID = "com.app.mobile.user.findUsersByMap";

	private static final String FIND_USERINFO_BY_ID = "zero2ipo.mobile.user.findUserInfoByUserId";
	private static final String FIND_USER_BY_MAP = "com.app.mobile.user.findUsersByMap";
	private static final String FIND_USER_BY_MAP_COUNT = "com.app.mobile.user.findUsersByMapCount";
	//根据邮箱查找用户
	private static final String FIND_USERS_BY_EMAIL = "zero2ipo.mobile.user.findUserByEmail";
	//保存用户信息
	private static final String INSERT_USER = "com.app.mobile.user.addAppUsers";
	//保存个人用户信息
	private static final String INSERT_USERINFO = "zero2ipo.mobile.user.saveUserInfo";
	//保存企业用户信息
	private static final String INSERT_ORGINFO = "zero2ipo.mobile.user.saveOrgInfo";
	//更新用户信息
	private static final String UPDATE_USERINFO = "zero2ipo.mobile.user.updateUserInfo";
	//更新企业用户信息
	private static final String UPDATE_ORGUSERINFO = "zero2ipo.mobile.user.updateOrgUserInfo";
	//更新企业用户信息
	private static final String UPDATE_CRMUSERINFO = "zero2ipo.mobile.user.updateCrmUserInfo";
	//修改用户密码
	private static final String UPDATE_USER_PASSWORD = "com.app.mobile.user.updateUserPassword";
	//修改登录次数
	private static final String UPDATE_USER_LOGINNUM = "zero2ipo.mobile.user.updateUserLoginNum";
	private static final String UPDATE_USER_MOBILE = "com.app.mobile.user.updateUserMobile";
	private static final String UPDATE_USER_OPENID = "com.app.mobile.user.updateUserOpenId";
	private static final String UPDATE_USER_QIANBAO = "com.app.mobile.user.updateUserQianBao";
	private static final String UPDATE_USER_QIANBAO_BY_OPENID = "com.app.mobile.user.updateUserQianBaoByOpenId";
	private static final String REDUCE_USER_QIANBAO = "com.app.mobile.user.reduceQianBao";
	private static final String FIND_ADMIN_BYMAP = "zero2ipo.mobile.admin.findUserById";
	private static final String FIND_ALL_ADMINS = "zero2ipo.mobile.admin.findUserInfoList";
	private static final String UPDATE_SEND_ORDER = "ggwash.mobile.sendOrder.updSendOrder";
	private static final String UPDATE_ADMIN = "zero2ipo.mobile.admin.upSysOper";

	/**
	 * 根据用户名密码查找用户
	 */
	public Users findUserByUsernameAndPassword(String username,
											   String password) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("mobile", username);
		if(!StringUtil.isNullOrEmpty(password)){
			param.put("password", password);
		}
		Users entity=null;
		try {
			 entity=(Users)this.query(FIND_USER_BY_USERNAME_PASSWORD, param);
		}catch (Exception e){
			e.printStackTrace();
		}
		return entity;
	}

	/**
	 * 根据用户手机号查找用户信息集合
	 */
	@SuppressWarnings("unchecked")
	public List<Users> findUserByMobile(String mobile) {
		try {
			Map<String,Object> queryMap=new HashMap<String, Object>();
			queryMap.put("mobile",mobile);
			List<?> o = this.queryAll(FIND_USERS_BY_MOBILE, queryMap);
			if(o != null)
			{
				List<Users> users = (List<Users>)o;
				return users;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 根据用户ID查找用户
	 * @param userId
	 * @return
	 */
	public Users findUserById(String userId) {
		Users u=null;
		try{
			Map map=new HashMap();
			map.put("userId",userId);
			u=(Users)this.query(FIND_USER_BY_ID, map);
		}catch (Exception e){
			u=new Users();
			e.printStackTrace();
		}
		return  u;
	}
	/**
	 * 注册用户信息
	 * @return
	 */
	public long saveUser(Users user) {
		long id=0;
		try{
			id=(Long)this.insert(INSERT_USER, user);
		}catch (Exception e){
			e.printStackTrace();
		}
		return id;
	}

	/**
	 * 注册个人用户信息
	 * @return
	 */
	public void saveUserInfo(UserEntity user) {

		this.insert(INSERT_USERINFO, user);
	}

	/**
	 * 注册企业用户信息
	 * @return
	 */
	public void updateUserQianBao(Users user) {
		try{
			this.update(UPDATE_USER_QIANBAO, user);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public void updateUserQianBaoByOpenId(Users u) {
		try{
			this.update(UPDATE_USER_QIANBAO_BY_OPENID, u);
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	public void reduceQianBao(Users user){
		try{
			this.update(REDUCE_USER_QIANBAO, user);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public List<AdminBo> findAdminList() {
		List<AdminBo> list=new ArrayList<AdminBo>();
		try{
			list= (List<AdminBo>) this.queryAll(FIND_ALL_ADMINS, null);
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int findUserByMapCount(Map<String, Object> map) {
		int count=0;
		try{
			count= (Integer) this.query(FIND_USER_BY_MAP_COUNT, map);
		}catch (Exception e){
			e.printStackTrace();
		}
		return count;
	}



	/**
	 * 更新个人用户信息
	 * @param user
	 */
	public void updateUserInfo(Users user) {
		try{
			this.update(UPDATE_USERINFO, user);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 更新机构用户信息
	 */
	public void updateOrgUserInfo(UserEntity user) {

		this.update(UPDATE_ORGUSERINFO, user);
	}

	/**
	 * 更新CRM用户信息
	 */
	public void updateCrmUserInfo(UserEntity user) {

		this.update(UPDATE_CRMUSERINFO, user);
	}

	/**
	 * 修改用户密码
	 */
	public void updateUserPassword(Users user) {

		this.update(UPDATE_USER_PASSWORD, user);
	}

	/**
	 * 根据邮箱查找用户
	 * @param email
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> findUserByEmail(String email) {

		Object o = this.queryAll(FIND_USERS_BY_EMAIL, email);
		if(o != null)
		{
			List<UserEntity> users = (List<UserEntity>)o;
			return users;
		}

		return null;
	}

	/**
	 * 更具手机好和密码查找用户
	 * @param mobile
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UserEntity> findUserByMobileAndPassword(String mobile,
			String password) {

		Map<String, String> param = new HashMap<String, String>();
		param.put("mobile", mobile);
		param.put("password", password);

		List<?> o = this.queryAll(FIND_USERS_BY_MOBILE_PWD, param);
		if(o != null)
		{
			List<UserEntity> users = (List<UserEntity>)o;
			return users;
		}

		return null;
	}

	/**
	 * 跟新用户登录次数
	 */
	public void updateUserLoginNum(UserEntity user) {

		user.setUserLoginnum(user.getUserLoginnum() + 1);
		try {
			this.update(UPDATE_USER_LOGINNUM, user);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
	/**
	 * 跟新用户信息
	 */
	public void updateUser(Users user){
		try{
			this.update(UPDATE_USER_OPENID, user);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	public boolean updateMobile(Users entity) {
		boolean flg=false;
		try {
			this.update(UPDATE_USER_MOBILE, entity);
			flg=true;
		} catch (Exception e) {
			// TODO: handle exception
			flg=false;
			e.printStackTrace();
		}
		return flg;
	}

	@Override
	public Users findUserByMap(Map<String, Object> queryMap) {
		Users u=null;
		try{
			u=(Users)this.query(FIND_USER_BY_MAP, queryMap);
		}catch (Exception e){
			//u=new UserEntity();
			e.printStackTrace();
		}
		return  u;
	}

	@Override
	public AdminBo findAdminLoginMessage(Map<String, Object> queryMap) {
		AdminBo admin=null;
		try{
			admin=(AdminBo)this.query(FIND_ADMIN_BYMAP, queryMap);
		}catch (Exception e){
			e.printStackTrace();
		}
		return admin;
	}

	@Override
	public boolean updateSendOrder(SendOrder order) {
		boolean flag=false;
		try {
			this.update(UPDATE_SEND_ORDER, order);
			flag=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public boolean updateAdmin(AdminBo admin) {
		boolean flag=false;
		try {
			this.update(UPDATE_ADMIN, admin);
			flag=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public AdminBo findAdminByLatLng(String lat, String lng) {
		AdminBo result=new AdminBo();
		try{
		//首先查询出所有的洗车工
		//按照职务过滤 此处应该根据角色过滤
		Map<String,Object> queryMap=new HashMap<String, Object>();
		//洗车工角色编码
			String  roleNo=CodeCommon.ROLE_NO;
		queryMap.put("roleNo", roleNo);
		List<AdminBo> adminBoList= (List<AdminBo>) this.queryAll(FIND_ALL_ADMINS,queryMap);
		int size=adminBoList.size();
		List<Double> areaList=new ArrayList<Double>();
		Map<Integer,Double> mapList=new HashMap<Integer, Double>();
		if(size>0){
			for(int i=0;i<size;i++){
				AdminBo bo=adminBoList.get(i);
				//获取经纬度
				String adminLng=bo.getEmail();//经度
				String adminLat=bo.getRemark();//纬度
				//获取该经纬度和当前位置之间的距离
				double lng1 = 0;
				double lat1=0;
				double lng2=0;
				double lat2=0;
				lng1=Double.parseDouble(lng);
				lat1=Double.parseDouble(lat);
				if(!StringUtil.isNullOrEmpty(adminLng)&&!StringUtil.isNullOrEmpty(adminLat)){
					lng2=Double.parseDouble(adminLng);
					lat2=Double.parseDouble(adminLat);
				}
				double d= DistanceUtil.GetDistance(lng1, lat1, lng2, lat2);
				//获取订单与洗车工距离之后，存放到容器中
				System.out.print("洗车工："+bo.getUserName()+"距离此订单的距离为===="+d);
				mapList.put(i,d);
			}
		}
		double k=0;
		int min=0;
		for(int i =0;i<mapList.size();i++)
		{
			if(i==0)
			{
				k=mapList.get(i);
				min=i;
			}
			else
			{
				if(k>=mapList.get(i))
				{
					k=mapList.get(i);
					min=i;
				}
			}
		}
		result=adminBoList.get(min);
		System.out.println("距离最近的洗车工为==========="+result.getUserName());
		}catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
