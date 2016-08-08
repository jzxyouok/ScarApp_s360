package com.zero2ipo.mobile.dao.bsb.impl;

import com.zero2ipo.common.entity.GgwashCoupon;
import com.zero2ipo.common.entity.UserCoupon;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.bsb.IWashCouponDao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component("washCouponDao")
public class WashCouponDaoImpl extends IbatisBaseDao implements IWashCouponDao {
	public final static String FIND_ALL_LIST="ggwash.mobile.coupon.findGgwash_couponList";
	public final static String FIND_BY_ID="ggwash.mobile.coupon.findCouponById";
	public final static String DEL_USERCOUPON_BYID="ggwash.couponbuy.delUserCouponById";
	public final static String FIND_BYID="ggwash.couponbuy.findById";
	public final static String FIND_USER_COUPON_BYID="ggwash.couponbuy.findUserCouponById";
	public final static String UPDATE_COUPON_NUM="ggwash.couponbuy.update";
	public final static String FIND_USER_COUPON_LIST="ggwash.couponbuy.findUser_couponList";
	public final static String FIND_USER_COUPON_ALL_LIST="ggwash.couponbuy.findAllList";
	public final static String FIND_USER_COUPON_LIST_COUNT="ggwash.couponbuy.findAllListCount";
	public final static String FIND_USER_COUPON_ALL_LIST_BYMOBILE="ggwash.couponbuy.findUserCouponByMoible";

	@Override
	public List<GgwashCoupon> findAllList(Map<String, Object> queryMap) {
		List<GgwashCoupon> list=null;
		try{
			list=(List<GgwashCoupon>) this.queryAll(FIND_ALL_LIST,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public GgwashCoupon findById(Map<String,Object> queryMap) {
		GgwashCoupon order=null;
		try{
			order=(GgwashCoupon) this.query(FIND_BY_ID,queryMap);
		}catch(Exception e){
			order=new GgwashCoupon();
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public void delUserCouponById(String couponId) {
		try {
				this.delete(DEL_USERCOUPON_BYID, couponId);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public UserCoupon findUserCouponById(Map<String, Object> queryMap) {
		UserCoupon userCoupon=null;
		try{
			userCoupon=(UserCoupon) this.query(FIND_USER_COUPON_BYID,queryMap);
		}catch(Exception e){
			//userCoupon=new UserCoupon();
			e.printStackTrace();
		}
		return userCoupon;
	}

	@Override
	public void updateCouponNum(UserCoupon userCoupon) {
		try {
			this.update(UPDATE_COUPON_NUM, userCoupon);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<UserCoupon> findUserCouponList(Map<String, Object> queryMap) {
		List<UserCoupon> list=null;
		try{
			list=(List<UserCoupon>) this.queryAll(FIND_USER_COUPON_LIST,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<UserCoupon> IsSd(Map<String, Object> queryMap) {
		List<UserCoupon> list=null;
		try{
			list=(List<UserCoupon>) this.queryAll(FIND_USER_COUPON_ALL_LIST_BYMOBILE,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int findAllListCount(Map<String, Object> queryMap) {
		int count=0;
		try{
			count=(Integer) this.query(FIND_USER_COUPON_LIST_COUNT, queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}

}
