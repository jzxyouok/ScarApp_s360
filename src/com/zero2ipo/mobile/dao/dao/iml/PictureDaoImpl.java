package com.zero2ipo.mobile.dao.dao.iml;

import com.zero2ipo.mobile.dao.base.IbatisBaseDao;
import com.zero2ipo.mobile.dao.dao.IPictureDao;
import com.zero2ipo.module.entity.picture.PictureEntity;
import com.zero2ipo.module.entity.picture.PictureTypeEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 图片数据Dao交互实现类
 * @author liyang
 *
 */
@Component("pictureDao")
public class PictureDaoImpl extends IbatisBaseDao implements IPictureDao {

	private static final String FIND_PICTURETYPE_BY_NAME = "zero2ipo.picture.findPictureTypeByName";
	private static final String FIND_PICTURES_BY_PID = "zero2ipo.picture.findPictureByPid";
	
	Logger log = Logger.getLogger(PictureDaoImpl.class);

	/*
	 * 根据名称查找类型数据
	 */
	public PictureTypeEntity findPictureTypeByName(String name) {

		return (PictureTypeEntity) this.query(FIND_PICTURETYPE_BY_NAME, name);
	}

	/*
	 * 根据类型id查找图片信息
	 */
	@SuppressWarnings("unchecked")
	public List<PictureEntity> findPicturesByPid(String pid) {

		return (List<PictureEntity>) this.queryAll(FIND_PICTURES_BY_PID, pid);
	}

}
