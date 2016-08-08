package com.zero2ipo.mobile.services.pic;

import com.zero2ipo.module.entity.picture.PictureEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图片服务层接口
 * @author liyang
 *
 */
@Service
public interface IPictureService {
	
	/*
	 * 根据PictureType类型名称查找Picture信息
	 */
	public List<PictureEntity> findPicturesByTypeName(String name);
	
}
