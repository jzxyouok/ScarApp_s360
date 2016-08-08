package com.zero2ipo.mobile.dao.student;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zero2ipo.common.entity.Student;

/**
 * cfjCollection 实体类
 * Thu Apr 30 11:19:31 GMT+08:00 2015 zhengyunfei
 */

@Service
public interface IStudentDao {
 /**
 *新增
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public int add(Student bo);

 public boolean update(Student Order);

public List<Student> findAllList(Map<String, Object> queryMap);

public Student findById(Map<String, Object> queryMap);

 
}

