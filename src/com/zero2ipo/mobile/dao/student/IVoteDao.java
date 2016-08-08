package com.zero2ipo.mobile.dao.student;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zero2ipo.common.entity.UserVote;
@Service
public interface IVoteDao {
	
	 public int add(UserVote bo);

	 public boolean update(UserVote Order);

	 public List<UserVote> findAllList(Map<String, Object> queryMap);

	 public UserVote findById(Map<String, Object> queryMap);
}
