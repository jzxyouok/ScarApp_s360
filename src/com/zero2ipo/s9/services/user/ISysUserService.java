package com.zero2ipo.s9.services.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zero2ipo.s9.entity.SysUserEntity;

/**
 * 后台用户服务层接口
 * @author zhengyunfei
 *
 */
@Service
public interface ISysUserService {

	/*
	 * 查找注册用户审核人员
	 */
	public List<SysUserEntity> findSysUsersByRoleNo();
	
}
