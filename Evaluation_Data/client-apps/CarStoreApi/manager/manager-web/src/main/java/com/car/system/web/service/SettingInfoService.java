package com.car.system.web.service;
/** 
* @author nwq
* @version 创建时间：2020年4月11日 上午11:45:38 
* 类说明 
*/

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.setting.SettingInfoListReq;
import com.car.system.client.request.setting.SettingInfoReq;
import com.car.system.client.request.setting.SettingInfoUpdateReq;
import com.car.system.web.model.SettingInfo;

import java.util.List;

public interface SettingInfoService {
	
	/**
	 * 根据id查询详情
	 */
	ResultRes<SettingInfo> getById(String uuid);
	
	/**
	 * 新增
	 */
	ResultRes<String> add(SettingInfoReq settingInfoReq);
	
	/**
	 * 根据id编辑
	 */
	ResultRes updateById(SettingInfoUpdateReq settingInfoUpdateReq);
	
	/**
	 * 根据id查询
	 */
	ResultRes deleteById(String uuid);
	
	/**
	 * 根据code获取列表
	 */
	ResultRes<Object> getByCode(String code);
	
	/**
	 * 分页列表
	 */
	PageRes<List<SettingInfo>> list(SettingInfoListReq settingInfoListReq);

}
