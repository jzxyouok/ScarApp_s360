package com.zero2ipo.car.chongzhi.bizc;


import com.zero2ipo.car.chongzhi.bo.ChongZhiBo;

import java.util.List;
import java.util.Map;


public interface IChongZhi{
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String add(ChongZhiBo bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String update(ChongZhiBo bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String delete(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public ChongZhiBo findById(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public List<ChongZhiBo> findAllList(Map<String, Object> queryMap);
	public int findAllListCount(Map<String, Object> map);

}

