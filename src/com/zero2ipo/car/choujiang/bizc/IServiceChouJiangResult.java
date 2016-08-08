package com.zero2ipo.car.choujiang.bizc;
import com.zero2ipo.car.choujiang.bo.ChouJiangResult;
import java.util.Map;
public interface IServiceChouJiangResult{
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String add(ChouJiangResult bo);
	public String update(ChouJiangResult bo);

	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public ChouJiangResult findById(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public int findAllListCount(Map<String, Object> map);
}

