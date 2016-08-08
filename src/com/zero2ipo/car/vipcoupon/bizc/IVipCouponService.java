package com.zero2ipo.car.vipcoupon.bizc;

import com.zero2ipo.car.vipcoupon.bo.VipCoupon;

import java.util.List;
import java.util.Map;


public interface IVipCouponService{
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String add(VipCoupon bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String update(VipCoupon bo);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public String delete(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public VipCoupon findById(String id);
	/**
	*@author zhengYunFei
	*@date Fri Nov 06 22:50:36 GMT+08:00 2015
	*/
	public List<VipCoupon> findAllList(Map<String, Object> queryMap);
	public int findAllListCount(Map<String, Object> map);

}

