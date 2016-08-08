package com.zero2ipo.mobile.services.bsb;

import com.zero2ipo.common.entity.Car;
import com.zero2ipo.common.entity.ServiceProject;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * cfjCollection 实体类
 * Thu Apr 30 11:19:31 GMT+08:00 2015 zhengyunfei
 */

@Service
public interface IHistoryCarService {
 /**
 *新增
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public int add(Car bo);
 /**
 *删除
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public boolean delete(Map<String, Object> queryMap);
 /**
 *根据Id查询
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public Car findById(Map<String, Object> queryMap);
 /**
  * 查询所有
  *
  */
 public List<Car> findAllList(Map<String, Object> queryMap);
 public boolean update(Car car);
 public ServiceProject getServiceProjectById(String id) ;
 public String getServiceProjectNames(String ids);
public int findAllListCount(Map<String, Object> queryMap);
}

