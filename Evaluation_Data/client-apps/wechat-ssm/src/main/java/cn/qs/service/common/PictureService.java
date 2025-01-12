package cn.qs.service.common;

import cn.qs.bean.common.Picture;

public interface PictureService {

	void insert(Picture picture);

	String getPicturePathByPictureId(String pictureId);
}
