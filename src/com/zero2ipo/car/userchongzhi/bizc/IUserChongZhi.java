package com.zero2ipo.car.userchongzhi.bizc;


import com.zero2ipo.car.userchongzhi.bo.UserChongZhiBo;

import java.util.List;
import java.util.Map;


public interface IUserChongZhi{
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String add(UserChongZhiBo bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String update(UserChongZhiBo bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String delete(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public UserChongZhiBo findById(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public List<UserChongZhiBo> findAllList(Map<String, Object> queryMap);
	public int findAllListCount(Map<String, Object> map);

}

