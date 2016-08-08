package com.zero2ipo.mobile.dao.bsb.impl;

import com.zero2ipo.common.entity.Address;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.bsb.IAddressDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("addressDao")
public class AddressDaoImpl extends IbatisBaseDao  implements IAddressDao{
   
	private static final String ADD = "bsb.mobile.address.add";
	private static final String FIND_BY_ID = "bsb.mobile.address.findById";
	private static final String FIND_ALL_LIST = "bsb.mobile.address.findAllList";
	
	public int add(Address bo) {
		int primkey=-1;
		try {
			primkey=(Integer) this.insert(ADD, bo);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return primkey;
	}

	@Override
	public Address findById(String id) {
		Address address=null;
		Map<String, Object> queryMap=new HashMap<String,Object>();
		queryMap.put("id",id);
		try{
			address=(Address) this.query(FIND_BY_ID, queryMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return address;
	}

	@Override
	public List<Address> findAllList(Map<String, Object> queryMap) {
		List<Address>  list=new ArrayList<Address>();
		try{
			list=(List<Address>) this.queryAll(FIND_ALL_LIST, queryMap);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
}

