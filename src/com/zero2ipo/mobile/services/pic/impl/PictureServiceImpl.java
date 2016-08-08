package com.zero2ipo.mobile.services.pic.impl;

import com.zero2ipo.mobile.dao.dao.IPictureDao;
import com.zero2ipo.mobile.services.pic.IPictureService;
import com.zero2ipo.module.entity.picture.PictureEntity;
import com.zero2ipo.module.entity.picture.PictureTypeEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片服务层接口实现类
 * @author liyang
 *
 */
@Component("pictureService")
public class PictureServiceImpl implements IPictureService {
	
	Logger log = Logger.getLogger(PictureServiceImpl.class);
	
	/*
	 * 根据PictureType类型名称查找Picture信息
	 */
	public List<PictureEntity> findPicturesByTypeName(String name) {
		
		List<PictureEntity> pictures = new ArrayList<PictureEntity>();
		try {
			PictureTypeEntity pictureType = pictureDao.findPictureTypeByName(name);
			if(pictureType != null && pictureType.getId() != null)
			{
				pictures = pictureDao.findPicturesByPid(pictureType.getId());
			}

		}catch (Exception e){
			e.printStackTrace();
		}

		return pictures;
	}
	
	@Resource(name = "pictureDao")
	private IPictureDao pictureDao;

}
