package com.zero2ipo.mobile.dao.comment.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zero2ipo.common.entity.Comment;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.comment.ICommentDao;
@Component("commentDao")
public class CommentDaoImpl extends IbatisBaseDao  implements ICommentDao{
	private static final String ADD="mobile.comment.addComment";
	private static final String FIND_BYID = "mobile.comment.findCommentById";
	@Override
	public boolean addComment(Comment comment) {
		boolean flg=false;
		try {
			this.insert(ADD, comment);
			flg=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return flg;
	}

	@Override
	public Comment updCommentInit(Map<String, Object> queryMap) {
		Comment comment=null;
		try {
			comment=(Comment) this.query(FIND_BYID,queryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comment;
	}


}
