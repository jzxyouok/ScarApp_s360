package com.zero2ipo.mobile.services.bsb;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.UserCoupon;

import java.util.List;
import java.util.Map;

public interface IWashCouponService {

	List<GgwashCoupon> findAllList(Map<String, Object> queryMap);

	public GgwashCoupon findById(Map<String, Object> queryMap) ;

	public void delUserCouponById(String couponId);

	public UserCoupon findUserCouponById(Map<String, Object> queryMap);

	public void updateCouponNum(UserCoupon userCoupon);

	public List<UserCoupon> findUserCouponList(Map<String, Object> queryMap);

	public List<UserCoupon> IsSd(Map<String, Object> queryMap);

	public int findAllListCount(Map<String, Object> queryMap);
}
