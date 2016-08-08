package com.zero2ipo.mobile.services.bsb.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.zero2ipo.common.entity.Address;
import com.zero2ipo.mobile.dao.bsb.IAddressDao;
import com.zero2ipo.mobile.services.bsb.IAddressService;

@Component("addressService")
public class AddressServiceImpl implements IAddressService{
	@Resource(name = "addressDao")
	private IAddressDao addressDao;
	public int add(Address bo) {
		return addressDao.add(bo);
	}
	@Override
	public Address findById(String id) {
		// TODO Auto-generated method stub
		return addressDao.findById(id);
	}
	@Override
	public List<Address> findAllList(Map<String, Object> queryMap) {
		// TODO Auto-generated method stub
		return addressDao.findAllList(queryMap);
	}
}

