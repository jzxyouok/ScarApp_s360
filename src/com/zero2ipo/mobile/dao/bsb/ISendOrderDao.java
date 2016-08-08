/**
 * @(#)IuserManage.java	10:10 07/08/2013
 *
 * Copyright (c) 2013 S9,Inc.All rights reserved.
 * Created by 2013-07-08
 */
package com.zero2ipo.mobile.dao.bsb;

import com.zero2ipo.common.entity.SendOrder;

import java.util.List;
import java.util.Map;


/**
 * @title: 派单业务处理接口定义
 * @description: 针对派单业务处理统一接口的定义类
 * @author： wangli
 * @date：2015-10-18
 */
public interface ISendOrderDao {

	public List<SendOrder> findSendOrderInfoList(Map<String, Object> query);
	public boolean updSendOrder(SendOrder sendOrder);
	public boolean startWashCar(SendOrder sendOrder);

	public SendOrder findSendOrderByOrderId(String id);
	public void addSendOrder(SendOrder sendOrder);
}
