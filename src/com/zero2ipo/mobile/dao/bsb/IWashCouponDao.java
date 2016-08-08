package com.zero2ipo.mobile.dao.bsb;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.UserCoupon;

import java.util.List;
import java.util.Map;

public interface IWashCouponDao {

	List<GgwashCoupon> findAllList(Map<String, Object> queryMap);

	public GgwashCoupon findById(Map<String, Object> queryMap) ;

	public void delUserCouponById(String id);

	public UserCoupon findUserCouponById(Map<String, Object> queryMap);

	public void updateCouponNum(UserCoupon userCoupon);

	public List<UserCoupon> findUserCouponList(Map<String, Object> queryMap);

	List<UserCoupon> IsSd(Map<String, Object> queryMap);

	public int findAllListCount(Map<String, Object> queryMap);
}
