package com.car.system.web.mapper;

import com.car.system.web.model.SettingInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/** 
* @author
* @version
* 模块配置
*/
@Repository
public interface SettingInfoMapper extends Mapper<SettingInfo> {

	/**
	 * 根据id查询
	 */
	SettingInfo getById(@Param("uuid") String uuid);
	
	/**
	 * 根据code查询
	 */
	SettingInfo getByCode(@Param("code") String code);
	
	/**
	 * 列表
	 */
	List<SettingInfo> getList();
	
}
