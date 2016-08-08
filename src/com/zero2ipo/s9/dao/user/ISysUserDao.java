package com.zero2ipo.s9.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zero2ipo.s9.entity.SysUserEntity;

/**
 * 后台用户数据交互DAO
 * @author zhengyunfei
 *
 */
@Repository
public interface ISysUserDao {
	
	/*
	 * 查找注册用户审核人员
	 */
	public List<SysUserEntity> findSysUsersByRoleNo();
	
}
