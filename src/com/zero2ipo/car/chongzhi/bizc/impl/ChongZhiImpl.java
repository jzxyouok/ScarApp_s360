package com.zero2ipo.car.chongzhi.bizc.impl;

import com.zero2ipo.car.chongzhi.bizc.IChongZhi;
import com.zero2ipo.car.chongzhi.bo.ChongZhiBo;
import com.zero2ipo.framework.exception.BaseException;
import com.zero2ipo.framework.log.BaseLog;
import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("chongzhi")
public class ChongZhiImpl extends IbatisBaseDao implements IChongZhi {
	private final  static String namespace="mobile.chongzhi.";
	private final static String  ADD=namespace+"addChongZhi";
	@Override
	public String add(ChongZhiBo bo) {
		String backInfo= "1";
		try{
			this.update(ADD, bo);
			backInfo= "1";
		}catch(Exception e){
			e.printStackTrace();
			backInfo= "0";
		    BaseLog.e(this.getClass(), "addOrder 添加联系人", e);
		    throw new BaseException("添加联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public String delete(String ids) {
		String backInfo = "0";
		try{
			Map map = new HashMap();
			map.put("id", ids.split(","));
			this.delete(namespace+"delChongZhi", map);
			//删除成功
			backInfo = "1";
		}catch(Exception e){
			backInfo = "0";	//删除失败
		    BaseLog.e(this.getClass(), "delOrderById 删除联系人", e);
		    throw new BaseException("删除联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public List<ChongZhiBo> findAllList(Map<String, Object> queryMap) {
		List<ChongZhiBo> list = new ArrayList<ChongZhiBo>();
		try {
			//设置数据库类型: 网站全局库(01)
			list = (List<ChongZhiBo>)this.queryAll(namespace+"findChongZhiList", queryMap);
		} catch (Exception e) {
			e.printStackTrace();
			BaseLog.e(this.getClass(), "queryOrderInfoList 查询列表失败", e);
		}
		return list;
	}

	@Override
	public ChongZhiBo findById(String id) {
		ChongZhiBo bo = null;
		try{
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", id);
			 bo = (ChongZhiBo)this.query(namespace+"findChongZhi", map);
		}catch(Exception e){
		    BaseLog.e(this.getClass(), "updInatitutionsInit 修改联系人前查询", e);
		    throw new BaseException("修改联系人前查询出错！",e);
		}
		return bo;
	}

	@Override
	public String update(ChongZhiBo bo) {
		String backInfo = "0";
		try{
			this.update(namespace+"updChongZhi", bo);
			backInfo = "1";
		}catch(Exception e){
			e.getStackTrace();
		    BaseLog.e(this.getClass(), "updOrder 修改联系人", e);
		    throw new BaseException("修改联系人出错！",e);
		}
		return backInfo;
	}

	@Override
	public int findAllListCount(Map<String, Object> map) {
		int count=0;
		try{
			this.query(namespace+"findChongZhiCount", map);
			count=1;
		}catch(Exception e){
			e.getStackTrace();
		    BaseLog.e(this.getClass(), "updOrder 修改联系人", e);
		    throw new BaseException("修改联系人出错！",e);
		}
		return count;
	}


}
