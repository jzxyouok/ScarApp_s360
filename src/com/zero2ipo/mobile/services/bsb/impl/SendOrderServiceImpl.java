package com.zero2ipo.mobile.services.bsb.impl;

import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.mobile.dao.bsb.ISendOrderDao;
import com.zero2ipo.mobile.services.bsb.ISendOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("sendOrderService")
public class SendOrderServiceImpl implements ISendOrderService{

	// 自动注入数据库公共操作接口
	@Autowired
	@Qualifier("sendOrderDao")
	private ISendOrderDao sendOrderDao;

	public boolean updSendOrder(SendOrder sendOrder){
		return sendOrderDao.updSendOrder(sendOrder);
	}
	public boolean startWashCar(SendOrder sendOrder){
		return sendOrderDao.startWashCar(sendOrder);
	}

	@Override
	public SendOrder findSendOrderByOrderId(String id) {
		return sendOrderDao.findSendOrderByOrderId(id);
	}

	@Override
	public void addSendOrder(SendOrder sendOrder) {
		sendOrderDao.addSendOrder(sendOrder);
	}

	@Override
	public List<SendOrder> findSendOrderInfoList(Map<String, Object> query) {
		return sendOrderDao.findSendOrderInfoList(query);
	}
}
