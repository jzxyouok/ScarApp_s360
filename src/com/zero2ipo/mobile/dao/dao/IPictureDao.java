package com.zero2ipo.mobile.dao.dao;

import com.zero2ipo.module.entity.picture.PictureEntity;
import com.zero2ipo.module.entity.picture.PictureTypeEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图片数据交互DAO
 * @author liyang
 *
 */
@Repository
public interface IPictureDao {
	
	/*
	 * 根据名称查找类型数据
	 */
	public PictureTypeEntity findPictureTypeByName(String name);
	
	/*
	 * 根据类型id查找图片信息
	 */
	public List<PictureEntity> findPicturesByPid(String pid);
	
}
