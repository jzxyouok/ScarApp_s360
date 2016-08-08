package com.zero2ipo.mobile.dao.bsb.impl;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.OrderTongJiResult;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.UserCoupon;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.bsb.IOrderDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("orderDao")
public class OrderDaoImpl extends IbatisBaseDao  implements IOrderDao{

	private static final String ADD = "bsb.mobile.order.add";
	private static final String UPDATE = "bsb.mobile.order.update";
	private static final String UPDATE_ORDER_STATUS = "bsb.mobile.order.updateStatus";
	private static final String UPDATE_OUTTRADENO = "bsb.mobile.order.updateOuttradeNo";
	private static final String UPDATE_ORDER_BY_OUTTRADENO = "bsb.mobile.order.updateOrderByOutTradeNo";
	private static final String FIND_ALL_LIST = "bsb.mobile.order.findAllList";
	private static final String FIND_ALL_FIRST_COUNT = "bsb.mobile.order.findAllListCount";
	private static final String FIND_BYID = "bsb.mobile.order.findById";
	private static final String INSERT_COUPON = "ggwash.couponbuy.addUserCoupon";
	private static final String FIND_SEND_ORDER_BYID="ggwash.mobile.sendOrder.findSendOrderById";
	private static final String FIND_ISORNOT_FIRST_ORDER = "bsb.mobile.order.findIsOrNotFirstOrder";
	private static final String FIND_TONGJI_RESULT = "bsb.mobile.order.findTongJiOrderResult";
	public int add(Order bo) {
		int primkey=-1;
		try {
			primkey=(Integer) this.insert(ADD, bo);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return primkey;
	}
	public boolean update(Order order) {
		boolean flag=true;
		try {
			this.update(UPDATE, order);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Order> findAllList(Map<String, Object> queryMap) {
		List<Order> list=null;
		try{
			list=(List<Order>) this.queryAll(FIND_ALL_LIST,queryMap);
		}catch(Exception e){
			list=new ArrayList<Order>();
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Order findById(Map<String, Object> queryMap) {
		Order order=null;
		try{
			order=(Order) this.query(FIND_BYID,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return order;
	}
	@Override
	public boolean updateCoupon(UserCoupon coupon) {
		boolean flag=true;
		try {
			this.insert(INSERT_COUPON, coupon);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}
	@Override
	public SendOrder findSendOrderByOrderId(Map<String, Object> map) {
		SendOrder order=null;
		try{
			order=(SendOrder) this.query(FIND_SEND_ORDER_BYID,map);
		}catch(Exception e){
			order=new SendOrder();
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public int findIsOrNotFirstOrder(Map<String, Object> queryMap) {
		int count=0;
		try{
			count=(Integer) this.query(FIND_ISORNOT_FIRST_ORDER,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}

		return count;
	}

	@Override
	public boolean updateStatus(Order order) {
		boolean flag=true;
		try {
			this.update(UPDATE_ORDER_STATUS, order);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;

	}

	@Override
	public int getTotal(Map<String, Object> queryMap) {
		int count=0;
		try{
			count=(Integer) this.query(FIND_ALL_FIRST_COUNT,queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public boolean updateOrderByOutTradeNo(Order order) {
		boolean flag=true;
		try {
			this.update(UPDATE_ORDER_BY_OUTTRADENO, order);
		} catch (Exception e) {
			e.printStackTrace();
			flag=false;
		}
		return flag;
	}

	@Override
	public void updateOuttradeNo(Order order) {
		try {
			this.update(UPDATE_OUTTRADENO, order);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public OrderTongJiResult findOrderTongJiResult(Map<String, Object> queryMap) {
		OrderTongJiResult tj=new OrderTongJiResult();
		try{
			tj=(OrderTongJiResult) this.query(FIND_TONGJI_RESULT,queryMap);
		}catch(Exception e){
			tj=new OrderTongJiResult();
			e.printStackTrace();
		}
		return tj;
	}

}

