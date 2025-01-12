package cn.qs.service.impl.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qs.bean.common.Picture;
import cn.qs.mapper.common.PictureMapper;
import cn.qs.service.common.PictureService;

@Service
@Transactional
public class PictureServiceImpl implements PictureService {

	@Autowired
	private PictureMapper pictureMapper;

	@Override
	public void insert(Picture picture) {
		pictureMapper.save(picture);
	}

	@Override
	public String getPicturePathByPictureId(String pictureId) {
		return pictureMapper.findOne(pictureId).getPath();
	}
}