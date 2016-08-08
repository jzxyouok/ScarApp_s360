package com.zero2ipo.car.choujiang.bizc.impl;
import com.zero2ipo.car.choujiang.bizc.IServiceChouJiangResult;
import com.zero2ipo.car.choujiang.bo.ChouJiangResult;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("choujiang")
public class ServiceChouJiangResultImpl extends IbatisBaseDao implements IServiceChouJiangResult {
	public final  static String COMMON="mobile.choujiang.";
	public final  static String ADD=COMMON+"add";
	public final  static String UPDATE=COMMON+"update";
	public final  static String GET_TOTAL=COMMON+"findAllListCount";
	public final  static String FIND_BYID=COMMON+"findById";
	@Override
	public String add(ChouJiangResult bo) {
		String backInfo= "1";
		try{
			this.insert(ADD, bo);
			backInfo= "1";
		}catch(Exception e){
			e.printStackTrace();
			backInfo= "0";
		}
		return backInfo;
	}
	@Override
	public String update(ChouJiangResult bo) {
		String backInfo= "1";
		try{
			this.insert(UPDATE, bo);
			backInfo= "1";
		}catch(Exception e){
			e.printStackTrace();
			backInfo= "0";
		}
		return backInfo;
	}

	@Override
	public ChouJiangResult findById(String id) {
		ChouJiangResult bo = null;
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", id);
			 bo = (ChouJiangResult)this.query(FIND_BYID, map);
		}catch(Exception e){
			e.printStackTrace();
		}
		return bo;
	}



	@Override
	public int findAllListCount(Map<String, Object> map) {
		int count=0;
		try{
			count=(Integer) this.query(GET_TOTAL, map);
		}catch(Exception e){
			e.getStackTrace();
		}
		return count;
	}

}
