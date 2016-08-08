package com.zero2ipo.car.project.bizc.impl;

import com.zero2ipo.car.project.bizc.IServiceProject;
import com.zero2ipo.car.project.bo.ServiceProject;
import com.zero2ipo.common.entity.app.Goodses;
import com.zero2ipo.framework.exception.BaseException;
import com.zero2ipo.framework.log.BaseLog;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("serviceProject")
public class ServiceProjectImpl extends IbatisBaseDao implements IServiceProject {
	public final  static String FINDALLLIST="mobile.goodses.findGoodsesList";
	@Override
	public String add(ServiceProject bo) {
		String backInfo= "1";
		try{

			this.update("addServiceProject", bo);
			backInfo= "1";
		}catch(Exception e){
			e.printStackTrace();
			backInfo= "0";
		    BaseLog.e(this.getClass(), "addOrder 添加联系人", e);
		    throw new BaseException("添加联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public String delete(String ids) {
		String backInfo = "0";
		try{
			Map map = new HashMap();
			map.put("id",ids.split(","));
			this.delete("delServiceProject", map);
			//删除成功
			backInfo = "1";
		}catch(Exception e){
			backInfo = "0";	//删除失败
		    BaseLog.e(this.getClass(), "delOrderById 删除联系人", e);
		    throw new BaseException("删除联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public List<Goodses> findAllList(Map<String, Object> queryMap) {
		List<Goodses> orderInfoList = new ArrayList<Goodses>();
		try {
			//设置数据库类型: 网站全局库(01)
    		orderInfoList = (List<Goodses>)this.queryAll(FINDALLLIST, queryMap);
		} catch (Exception e) {
			e.printStackTrace();
			BaseLog.e(this.getClass(), "queryOrderInfoList 查询列表失败", e);
		}
		return orderInfoList;
	}

	@Override
	public ServiceProject findById(String id) {
		ServiceProject bo = null;
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", id);
			 bo = (ServiceProject)this.query("findServiceProject", map);
		}catch(Exception e){
		    BaseLog.e(this.getClass(), "updInatitutionsInit 修改联系人前查询", e);
		    throw new BaseException("修改联系人前查询出错！",e);
		}
		return bo;
	}

	@Override
	public String update(ServiceProject bo) {
		String backInfo = "0";
		try{

			this.update("updServiceProject", bo);
			backInfo = "1";
		}catch(Exception e){
			e.getStackTrace();
		    BaseLog.e(this.getClass(), "updOrder 修改联系人", e);
		    throw new BaseException("修改联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public int findAllListCount(Map<String, Object> map) {
		int count=0;
		try{
			this.query("findServiceProjectCount", map);
			count=1;
		}catch(Exception e){
			e.getStackTrace();
		    BaseLog.e(this.getClass(), "updOrder 修改联系人", e);
		    throw new BaseException("修改联系人出错！",e);
		}
		return count;
	}

}
