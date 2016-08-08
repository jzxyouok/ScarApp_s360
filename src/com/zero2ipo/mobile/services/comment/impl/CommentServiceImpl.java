package com.zero2ipo.mobile.services.comment.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zero2ipo.common.entity.Comment;
import com.zero2ipo.mobile.dao.comment.ICommentDao;
import com.zero2ipo.mobile.services.comment.ICommentService;
@Component("commentService")
public class CommentServiceImpl implements ICommentService{
@Resource(name = "commentDao")
private ICommentDao commentDao;
	@Override
	public boolean addComment(Comment comment) {
		return commentDao.addComment(comment);
	}

	@Override
	public Comment updCommentInit(Map<String, Object> queryMap) {
		return commentDao.updCommentInit(queryMap);
	}


}
