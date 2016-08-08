package com.zero2ipo.mobile.dao.student.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zero2ipo.common.entity.UserVote;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.student.IVoteDao;

@Component("voteDao")
public class VoteDaoImpl extends IbatisBaseDao  implements IVoteDao{
   
	private static final String ADD = "vote.mobile.toupiao.add";
	private static final String UPDATE = "vote.mobile.toupiao.update";
	private static final String FIND_ALL_LIST = "vote.mobile.toupiao.findAllList";
	private static final String FIND_BYID = "vote.mobile.toupiao.findById";
	public int add(UserVote bo) {
		int primkey=-1;
		try {
			primkey=(Integer) this.insert(ADD, bo);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return primkey;
	}
	public boolean update(UserVote order) {
		boolean flag=true;
		try {
			this.update(UPDATE, order);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<UserVote> findAllList(Map<String, Object> queryMap) {
		List<UserVote> list=null;
		try{
			list=(List<UserVote>) this.queryAll(FIND_ALL_LIST,queryMap);
		}catch(Exception e){
			list=new ArrayList<UserVote>();
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public UserVote findById(Map<String, Object> queryMap) {
		UserVote bo=null;
		try{
			bo=(UserVote) this.query(FIND_BYID,queryMap);
		}catch(Exception e){
			bo=new UserVote();
			e.printStackTrace();
		}
		return bo;
	}
	
	
}

