package com.zero2ipo.mobile.dao.student.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zero2ipo.common.entity.Student;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.student.IStudentDao;

@Component("studentDao")
public class StudentDaoImpl extends IbatisBaseDao  implements IStudentDao{
   
	private static final String ADD = "vote.mobile.baoming.add";
	private static final String UPDATE = "vote.mobile.baoming.update";
	private static final String FIND_ALL_LIST = "vote.mobile.baoming.findAllList";
	private static final String FIND_BYID = "vote.mobile.baoming.findById";
	public int add(Student bo) {
		int primkey=-1;
		try {
			primkey=(Integer) this.insert(ADD, bo);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return primkey;
	}
	public boolean update(Student order) {
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
	public List<Student> findAllList(Map<String, Object> queryMap) {
		List<Student> list=new ArrayList<Student>();
		try{
			list=(List<Student>) this.queryAll(FIND_ALL_LIST,queryMap);
		}catch(Exception e){
			list=new ArrayList<Student>();
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Student findById(Map<String, Object> queryMap) {
		Student Student=null;
		try{
			Student=(Student) this.query(FIND_BYID,queryMap);
		}catch(Exception e){
			Student=new Student();
			e.printStackTrace();
		}
		return Student;
	}
	
	
}

