package com.zero2ipo.mobile.dao.config.impl;

import com.zero2ipo.common.entity.ConfValue;
import com.zero2ipo.framework.util.StringUtil;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.config.IConfigDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("configDao")
public class ConfigDaoImpl extends IbatisBaseDao  implements IConfigDao{

	private static final String findConfValueByMap = "mobile.config.weixin.queryConfValue";


	@Override
	public ConfValue findConfValueByMap(Map<String, Object> m) {
		ConfValue confValue=null;
		try{
			confValue=(ConfValue) this.query(findConfValueByMap, m);
		}catch(Exception e){
			e.printStackTrace();
		}
		return confValue;
	}

	public String getValue(String key) {
		String result="";
		Map<String, Object> m=new HashMap<String, Object>();
		m.put("confKey",key);
		ConfValue  confValue=(ConfValue) this.query(findConfValueByMap, m);
		if(!StringUtil.isNullOrEmpty(confValue)){
			result=confValue.getConfValue();
		}
		return result;
	}


}

