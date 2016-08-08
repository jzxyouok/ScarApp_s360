package com.zero2ipo.s9.dao.user.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.s9.dao.user.ISysUserDao;
import com.zero2ipo.s9.entity.SysUserEntity;

/**
 * 后台用户数据交互DAO
 * @author zhengyunfei
 *
 */
@Component("sysUserDao")
public class SysUserDaoImpl extends IbatisBaseDao implements ISysUserDao {

	private static final String FIND_SYSUSER_ROLENO = "zero2ipo.sys.user.findSysUsersByRoleNo";
	
	@SuppressWarnings("unchecked")
	public List<SysUserEntity> findSysUsersByRoleNo() {
		
		List<SysUserEntity> sues = new ArrayList<SysUserEntity>();
		List<?> o = this.queryAll(FIND_SYSUSER_ROLENO);
		if(o != null) 
		{
			sues = (List<SysUserEntity>)o;
		}
		
		return sues;
	}
	

}
