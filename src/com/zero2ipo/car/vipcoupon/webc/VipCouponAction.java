package com.zero2ipo.car.vipcoupon.webc;

import com.zero2ipo.car.vipcoupon.bizc.IVipCouponService;
import com.zero2ipo.car.vipcoupon.bo.VipCoupon;
import com.zero2ipo.common.entity.app.Users;
import com.zero2ipo.common.web.BaseCtrl;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.utils.DateUtil;
import com.zero2ipo.mobile.web.SessionHelper;
import com.zero2ipo.module.entity.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
/**
 * 优惠券和会员关系对应表
 * @author zhengyunfei
 *
 */
@Controller
@RequestMapping("/vc")
public class VipCouponAction extends BaseCtrl{

	@Autowired @Qualifier("vipCouponService")
	private IVipCouponService vipCoupon;
  /**
   *兑换优惠券
   * @return
   */
  @RequestMapping("tuihuan.html")
  @ResponseBody
  public Map<String,Object> addSave(String couponCode) {
	Map<String,Object> resultMap=new HashMap<String,Object>();
	Users userEntity=(Users) SessionHelper.getAttribute(request, MobileContants.USER_SESSION_KEY);
	String userId="";
	if(!StringUtil.isNullOrEmpty(userEntity)){
		userId=userEntity.getUserId();
	}
  	try{
  		boolean flag=false;
  		//根据优惠券编码查询优惠券信息
  		Map<String, Object> queryMap=new HashMap<String, Object>();
  		queryMap.put("couponCode", couponCode);
  		List<VipCoupon> list=vipCoupon.findAllList(queryMap);
  		if(!StringUtil.isNullOrEmpty(list)&&list.size()>0){
			VipCoupon coupon=list.get(0);
  			//判断优惠券是否被使用
  			Map<String,Object> m=new HashMap<String, Object>();
  			m.put("couponCode", coupon.getCouponCode());
  			List<VipCoupon> useList=vipCoupon.findAllList(m);
  			if(!StringUtil.isNullOrEmpty(useList)&&useList.size()>0){//已经被使用了
  				resultMap.put("success", "1");//已经被使用了
  			}else{

  				//判断优惠券是否已经过期
  				String day=DateUtil.getCurrentDateStr().substring(0,10);
  				String endTimee=coupon.getCouponEndTime();
  				Date start=StringUtil.str2SqlDate(day, "yyyy-MM-dd");
  				Date end=StringUtil.str2SqlDate(endTimee, "yyyy-MM-dd");
  				int count=end.getDate()-start.getDate();
  				if(count>=0){//没有过期
  					//将优惠券放到该会员的账号中
  					VipCoupon bo=new VipCoupon();
  					bo.setCouponCode(coupon.getCouponCode());
  					bo.setCouponStartTime(coupon.getCouponStartTime());
  					bo.setCouponEndTime(coupon.getCouponEndTime());
  					bo.setCouponMoney(coupon.getCouponMoney());
  					bo.setCouponName(coupon.getCouponName());
  					bo.setCouponRemark(coupon.getCouponRemark());
  					bo.setStatus(coupon.getStatus());
  					bo.setUserId(userId);
  					vipCoupon.add(bo);
					flag=true;
  					resultMap.put("success", "0");//未被使用

  				}else {//已过期
  					resultMap.put("success", "2");//已经过期了
				}
  			}
  			//将

  		}else{
  			resultMap.put("success", "-1");//优惠券不存在
  		}

  	}catch (Exception e) {
  		e.printStackTrace();
  		resultMap.put("success", "error");
	}
	return resultMap;
  }

public static void main(String[] args) throws DataFormatException {
	String value=DateUtil.getCurrentDateStr().substring(0,10);
	Date d1=StringUtil.str2SqlDate(value,"yyyy-MM-dd");
	Date d2=StringUtil.str2SqlDate("2015-12-01", "yyyy-MM-dd");
	System.out.println(d1.getDate()-d2.getDate());
	System.out.println(6%5);
}

}
