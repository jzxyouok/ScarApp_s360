package com.zero2ipo.mobile.dao.bsb;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zero2ipo.common.entity.Address;

/**
 * cfjCollection 实体类
 * Thu Apr 30 11:19:31 GMT+08:00 2015 zhengyunfei
 */

@Service
public interface IAddressDao {
 /**
 *新增
 *@author zhengYunFei
 *@date Thu Apr 30 11:19:31 GMT+08:00 2015
 */
 public int add(Address bo);

public Address findById(String id);

public List<Address> findAllList(Map<String, Object> queryMap);

 
 
}

