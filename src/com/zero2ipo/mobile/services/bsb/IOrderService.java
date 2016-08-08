package com.zero2ipo.mobile.services.bsb;

import com.zero2ipo.common.entity.Order;
import com.zero2ipo.common.entity.OrderTongJiResult;
import com.zero2ipo.common.entity.SendOrder;
import com.zero2ipo.common.entity.UserCoupon;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * cfjCollection 实体类
 * Thu Apr 30 11:19:31 GMT+08:00 2015 zhengyunfei
 */

@Service
public interface IOrderService {
 /**
 *新增
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public int add(Order bo);

 public boolean update(Order Order);

public List<Order> findAllList(Map<String, Object> queryMap);

public Order findById(Map<String, Object> queryMap);
/**
 * 保存优惠券和用户关系表
 * @param coupon
 * @return
 */
public boolean updateCoupon(UserCoupon coupon);

/*柑根据 订单 id查询 洗车作业*/

public SendOrder findSendOrderByOrderId(Map<String, Object> map);

public int findIsOrNotFirstOrder(Map<String, Object> queryMap);

 public boolean updateStatus(Order order);

 public int getTotal(Map<String, Object> queryMap);

 public boolean updateOrderByOutTradeNo(Order order);

 public void updateOuttradeNo(Order order);

 public OrderTongJiResult findOrderTongJiResult(Map<String, Object> queryMap);
}

