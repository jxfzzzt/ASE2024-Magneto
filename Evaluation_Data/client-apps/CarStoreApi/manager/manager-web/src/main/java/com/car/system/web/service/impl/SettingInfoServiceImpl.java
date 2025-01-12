package com.car.system.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.UuidUtils;
import com.car.system.client.request.setting.SettingInfoListReq;
import com.car.system.client.request.setting.SettingInfoReq;
import com.car.system.client.request.setting.SettingInfoUpdateReq;
import com.car.system.web.mapper.SettingInfoMapper;
import com.car.system.web.model.SettingInfo;
import com.car.system.web.service.SettingInfoService;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
* @author
* @version
* 模块配置 
*/
@Slf4j
@Service
public class SettingInfoServiceImpl implements SettingInfoService {
	
	@Autowired
	private SettingInfoMapper settingInfoMapper;
	
	/**
	 * 根据id查询详情
	 */
	@Override
	public ResultRes<SettingInfo> getById(String uuid) {
		if(StringUtils.isBlank(uuid)){
            throw new BusinessException(ResEnum.VALID_PARAM_ERROR);
        }
		SettingInfo settingInfo = settingInfoMapper.getById(uuid);
		return ResultRes.success(settingInfo);
	}

	/**
	 * 新增
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@LcnTransaction
	public ResultRes<String> add(SettingInfoReq settingInfoReq) {

		SettingInfo settingInfo = new SettingInfo();
		settingInfo.setCode(settingInfoReq.getCode());
		settingInfo = settingInfoMapper.selectOne(settingInfo);
		if (settingInfo != null) {
			throw new BusinessException(ResEnum.CODE_EXIST);
		}
		SettingInfo addSettingInfo = new SettingInfo();
		String userName = TokenHelper.getUserName();
		String uuid = UuidUtils.getUuid();
		BeanUtils.copyProperties(settingInfoReq,addSettingInfo);
		addSettingInfo.setUuid(uuid);
		addSettingInfo.setCreatedTime(new Date());
		addSettingInfo.setCreatedBy(userName);
		addSettingInfo.setSts(StsEnum.ACTIVE.getValue());
		settingInfoMapper.insert(addSettingInfo);
		return ResultRes.success(uuid);
	}

	/**
	 * 编辑
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@LcnTransaction
	public ResultRes updateById(SettingInfoUpdateReq settingInfoUpdateReq) {

		SettingInfo settingInfoByKey = settingInfoMapper.selectByPrimaryKey(settingInfoUpdateReq.getUuid());
		if (settingInfoByKey == null || StsEnum.INVALID.getValue().equals(settingInfoByKey.getSts())) {
			throw new BusinessException(ResEnum.NON_EXISTENT);
		}
		SettingInfo settingInfoByCode = new SettingInfo();
		settingInfoByCode.setCode(settingInfoUpdateReq.getCode());
		settingInfoByCode.setSts(StsEnum.ACTIVE.getValue());
		settingInfoByCode = settingInfoMapper.selectOne(settingInfoByCode);
		if (settingInfoByCode != null && settingInfoByKey.getCode().equals(settingInfoByCode.getCode())) {
			throw new BusinessException(ResEnum.CODE_EXIST);
		}

		String userName = TokenHelper.getUserName();
		SettingInfo settingInfo = new SettingInfo();
		BeanUtils.copyProperties(settingInfoUpdateReq,settingInfo);
		settingInfo.setLastUpdatedBy(userName);
		settingInfo.setLastUpdatedTime(new Date());
		settingInfoMapper.updateByPrimaryKeySelective(settingInfo);
		return ResultRes.success();
	}

	/**
	 * 根据id删除
	 */
	@Override
	public ResultRes deleteById(String uuid) {
		if(StringUtils.isBlank(uuid)){
            throw new BusinessException(ResEnum.VALID_PARAM_ERROR);
        }
		String userName = TokenHelper.getUserName();
		SettingInfo settingInfo = new SettingInfo();
		settingInfo.setUuid(uuid);
		settingInfo.setLastUpdatedBy(userName);
		settingInfo.setLastUpdatedTime(new Date());
		settingInfo.setSts(StsEnum.INVALID.getValue());
		settingInfoMapper.updateByPrimaryKeySelective(settingInfo);
		return ResultRes.success();
	}

	/**
	 * 根据code获取列表
	 */
	@Override
	public ResultRes<Object> getByCode(String code) {
		Object object = null;
		SettingInfo settingInfo = settingInfoMapper.getByCode(code);
		if (settingInfo != null) {
			object = JSONObject.parse(settingInfo.getContent());
		}
		return ResultRes.success(object);
	}

	/**
	 * 分页获取
	 */
	@Override
	public PageRes<List<SettingInfo>> list(SettingInfoListReq settingInfoListReq) {
		Integer pageNum = settingInfoListReq.getPageNum();
        Integer pageSize = settingInfoListReq.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        List<SettingInfo> list = settingInfoMapper.getList();
        PageInfo<SettingInfo> pageInfo = new PageInfo<>(list);
        PageHelper.clearPage();
        return PageRes.success(list, pageInfo.getPageSize(), (int) pageInfo.getTotal(), pageInfo.getPages());
	}

}
