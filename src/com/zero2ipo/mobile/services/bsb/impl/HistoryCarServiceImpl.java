package com.zero2ipo.mobile.services.bsb.impl;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.ServiceProject;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.dao.bsb.IHistoryDao;
import com.zero2ipo.mobile.services.bsb.IHistoryCarService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("historyCarService")
public class HistoryCarServiceImpl implements IHistoryCarService{
	/*
	 * 基金DAO注入
	 */
	@Resource(name = "historyCarDao")
	private IHistoryDao historyCarDao;
	public int add(Car bo) {
		return historyCarDao.add(bo);
	}
	public boolean update(Car bo) {
		return historyCarDao.update(bo);
	}
	public boolean delete(Map<String,Object> queryMap) {
		return historyCarDao.delete(queryMap);
	}
	public Car findById(Map<String, Object> queryMap) {
		return historyCarDao.findById(queryMap);
	}
	 public ServiceProject getServiceProjectById(String id) {
		 return historyCarDao.getServiceProjectById(id);
	 }
	 public int findAllListCount(Map<String, Object> queryMap){
		 return historyCarDao.findAllListCount(queryMap);
	 }
	/**
	 * 查询所有
	 * 
	 */
	public List<Car> findAllList(Map<String, Object> queryMap){
		return historyCarDao.findAllList(queryMap);
	}
	public String getServiceProjectNames(String ids){
		String names="";
		String [] strs=ids.split(",");
		for(int i=0;i<strs.length;i++){
			if(!"".equals(strs[i])){
				ServiceProject bo=this.getServiceProjectById(strs[i]);
				if(!StringUtil.isNullOrEmpty(bo)){
					if(i<strs.length-1){
						names+=bo.getName()+",";
					}else{
						names+=bo.getName();
					}
					
				}
			}
				
		}
		return names;
	}
}

