package com.zero2ipo.car.project.bizc;

import com.zero2ipo.car.project.bo.ServiceProject;
import com.zero2ipo.common.entity.app.Goodses;

import java.util.List;
import java.util.Map;


public interface IServiceProject{
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String add(ServiceProject bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String update(ServiceProject bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String delete(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public ServiceProject findById(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public List<Goodses> findAllList(Map<String, Object> queryMap);
	public int findAllListCount(Map<String, Object> map);
}

