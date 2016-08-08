package com.zero2ipo.mobile.dao.bsb.impl;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.core.MobileContants;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.bsb.ISendOrderDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("sendOrderDao")
public class SendOrderDaoImpl extends IbatisBaseDao implements ISendOrderDao{
	private static final String findSendOrderListByXcg="ggwash.mobile.sendOrder.findSendOrderList";
	private static final String findSendOrderByOrderId="ggwash.mobile.sendOrder.findSendOrderByOrderId";
	private static final String UPDATE_SEND_ORDER="ggwash.mobile.sendOrder.updSendOrder";
	private static final String ADD="ggwash.mobile.sendOrder.addSendOrder";
	private static final String UPDATE_ORDER = "bsb.mobile.order.update";
	/**
	 * 根据洗车工id查询洗车的订单
	 */
	@Override
	public List<SendOrder> findSendOrderInfoList(Map<String, Object> queryMap) {
		List<SendOrder> list=null;
		try {
			list=(List<SendOrder>) this.queryAll(findSendOrderListByXcg,queryMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 完成洗车
	 */
	@Override
	public boolean updSendOrder(SendOrder sendOrder) {
		boolean flag=true;
		try {
			this.update(UPDATE_SEND_ORDER, sendOrder);
			//修改任务成功之后，修改订单的派单状态
			Order order=new Order();
			order.setSendOrderStatus(sendOrder.getStatus());
			order.setId(Integer.parseInt(sendOrder.getOrderId()));
			order.setOrderStatus(MobileContants.status_1);//洗车完毕后,修改订单的支付状态为已支付
			this.update(UPDATE_ORDER, order);
		} catch (Exception e) {
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 开始洗车
	 */
	@Override
	public boolean startWashCar(SendOrder sendOrder) {
		boolean flag=false;
		try {
			this.update(UPDATE_SEND_ORDER, sendOrder);
			//修改任务成功之后，修改订单的派单状态
			Order order=new Order();
			order.setSendOrderStatus(sendOrder.getStatus());
			order.setId(Integer.parseInt(sendOrder.getOrderId()));
			this.update(UPDATE_ORDER, order);
			flag=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	@Override
	public SendOrder findSendOrderByOrderId(String id) {
		SendOrder sendOrder=null;
		List<SendOrder> list=null;
		try {
			Map<String,Object> queryMap=new HashMap<String, Object>();
			queryMap.put("orderId",id);
			list=(List<SendOrder>) this.queryAll(findSendOrderByOrderId,queryMap);
			if(!com.zero2ipo.framework.util.StringUtil.isNullOrEmpty(list)&&list.size()>0){
				sendOrder=list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendOrder;
	}

	@Override
	public void addSendOrder(SendOrder sendOrder) {
		try {
			this.insert(ADD,sendOrder);
			//新增派单后，修改订单状态为已派单
			Order order=new Order();
			order.setSendOrderStatus(sendOrder.getStatus());
			order.setId(Integer.parseInt(sendOrder.getOrderId()));
			this.update(UPDATE_ORDER, order);
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}
