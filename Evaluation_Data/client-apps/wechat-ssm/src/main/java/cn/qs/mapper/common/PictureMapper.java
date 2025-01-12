package cn.qs.mapper.common;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qs.bean.common.Picture;

public interface PictureMapper extends JpaRepository<Picture, String> {

}