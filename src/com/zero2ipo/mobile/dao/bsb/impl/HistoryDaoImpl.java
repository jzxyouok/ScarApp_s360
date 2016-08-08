package com.zero2ipo.mobile.dao.bsb.impl;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.ServiceProject;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.bsb.IHistoryDao;
import com.zero2ipo.mobile.services.dictionary.impl.DictionaryServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("historyCarDao")
public class HistoryDaoImpl extends IbatisBaseDao  implements IHistoryDao{
    private static final String FIND_COLLECTION_BYID = "bsb.mobile.car.findCarById";
    private static final String FIND_SERVICEPROJECT_BYID = "bsb.mobile.serviceproject.findById";
	private static final String CAR_ADD = "bsb.mobile.car.addCar";
	private static final String CAR_UPDATE = "bsb.mobile.car.updCar";
	private static final String FIND_ALL_LIST="bsb.mobile.car.findCarList";
	private static final String FIND_ALL_LIST_COUNT="bsb.mobile.car.findCarListCount";
	Logger log = Logger.getLogger(DictionaryServiceImpl.class);
	/**
	 * 收藏
	 */
	public int add(Car bo) {
		int primkey=-1;
		try {
			primkey=(Integer) this.insert(CAR_ADD, bo);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return primkey;
	}
	public boolean update(Car bo) {
		boolean flag=true;
		try {
			this.update(CAR_UPDATE, bo);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	/**
	 * 取消收藏
	 */
	public boolean delete(Map<String, Object> queryMap) {
		int count=0;
		count=this.delete("", queryMap);
		if(count==0){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * 查询收藏
	 */
	public Car findById(Map<String, Object> queryMap) {
		Car entity=null;
		try{
			entity=(Car) this.query(FIND_COLLECTION_BYID, queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return entity;
	}
	 public ServiceProject getServiceProjectById(String id) {
		 ServiceProject entity=null;
		try{
			Map<String,Object> queryMap=new HashMap<String,Object>();
			queryMap.put("id", id);
			entity=(ServiceProject) this.query(FIND_SERVICEPROJECT_BYID, queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return entity;
	}
	
	/**
	 * 查询所有
	 * @param queryMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Car> findAllList(Map<String, Object> queryMap){
		List<Car> list=null;
		try{
			list=(List<Car>) this.queryAll(FIND_ALL_LIST, queryMap);
		}catch (Exception e) {
			list=new ArrayList<Car>();
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public int findAllListCount(Map<String, Object> queryMap) {
		int count=0;
		try{
			count=(Integer)this.query(FIND_ALL_LIST_COUNT, queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return count;
	}
	
}

