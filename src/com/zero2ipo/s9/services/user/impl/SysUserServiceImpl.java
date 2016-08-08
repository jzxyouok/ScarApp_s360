package com.zero2ipo.s9.services.user.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zero2ipo.s9.dao.user.ISysUserDao;
import com.zero2ipo.s9.entity.SysUserEntity;
import com.zero2ipo.s9.services.user.ISysUserService;

/**
 * 后台用户服务层接口实现
 * @author zhengyunfei
 *
 */
@Component("sysUserService")
public class SysUserServiceImpl implements ISysUserService {

	/*
	 * 查找注册用户审核人员
	 */
	public List<SysUserEntity> findSysUsersByRoleNo() {
		
		return sysUserDao.findSysUsersByRoleNo();
	}
	
	/*
	 * 后台用户DAO接口注入
	 */
	@Resource(name = "sysUserDao")
	private ISysUserDao sysUserDao;

}
