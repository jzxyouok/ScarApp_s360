package com.zero2ipo.mobile.services.bsb.impl;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.UserCoupon;
import com.zero2ipo.mobile.dao.bsb.IWashCouponDao;
import com.zero2ipo.mobile.services.bsb.IWashCouponService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("washCouponService")
public class WashCouponServiceImpl implements IWashCouponService{
	@Resource(name = "washCouponDao")
	private IWashCouponDao washCouponDao;
	@Override
	public GgwashCoupon findById(Map<String, Object> queryMap) {
		return washCouponDao.findById(queryMap);
	}
	@Override
	public List<GgwashCoupon> findAllList(Map<String, Object> queryMap) {
		return washCouponDao.findAllList(queryMap);
	}
	@Override
	public void delUserCouponById(String id) {
		 washCouponDao.delUserCouponById(id);

	}
	@Override
	public UserCoupon findUserCouponById(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return washCouponDao.findUserCouponById(queryMap);
	}
	@Override
	public void updateCouponNum(UserCoupon userCoupon) {
		washCouponDao.updateCouponNum(userCoupon);

	}
	@Override
	public List<UserCoupon> findUserCouponList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return washCouponDao.findUserCouponList(queryMap);
	}
	@Override
	public List<UserCoupon> IsSd(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return washCouponDao.IsSd(queryMap);
	}

	@Override
	public int findAllListCount(Map<String, Object> queryMap) {
		 return washCouponDao.findAllListCount(queryMap);
	}
}

