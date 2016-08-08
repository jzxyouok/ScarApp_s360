/**
 * @(#)IuserManage.java	10:10 07/08/2013
 * 
 * Copyright (c) 2013 S9,Inc.All rights reserved.
 * Created by 2013-07-08 
 */
package com.zero2ipo.mobile.dao.comment;

import java.util.Map;

import com.zero2ipo.common.entity.Comment;


/**
 * @title: 系统评论信息管理业务处理接口定义
 * @description: 针对系统评论信息管理业务处理统一接口的定义类
 * @author： wangli
 * @date：2015-10-18
 */
public interface ICommentDao {

	
	/**
	 * @title： 评论信息    新增
	 * @description: 存储系统评论的信息,评论对评论信息的添加操作
	 * @param: comment  系统评论信息实体类
	 * @author: wangli
	 */
	public boolean addComment(Comment comment);

	/**
	 * @title： 评论信息修改页面初始化
	 * @description: 对不同系统评论信息的查询修改数据初始化
	 * @param: commentId   评论标识
	 * @author: wangli
	 * @return： comment   系统评论信息实体类
	 */
	public Comment updCommentInit(Map<String, Object> queryMap);

	
	
}
