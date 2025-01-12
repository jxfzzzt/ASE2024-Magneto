package com.car.account.web.service.vehicle.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.car.account.client.response.vehicle.config.ConfigRes;
import com.car.account.web.common.utils.UuidUtils;
import com.car.account.web.mapper.vehicle.VehicleConfigMapper;
import com.car.account.web.model.vehicle.VehicleConfig;
import com.car.account.web.service.vehicle.VehicleConfigService;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.ExcelUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author xlj
 */
@Slf4j
@Service
public class VehicleConfigServiceImpl implements VehicleConfigService {

	@Autowired
	VehicleConfigMapper vehicleConfigMapper;

	/**
	 * 查询所有车辆配置信息
	 * @return
	 */
	@Override
	public ResultRes<List<ConfigRes>> queryAllList() {
		// 查询所有有效的数据信息
		VehicleConfig search = new VehicleConfig();
		search.setSts(StsEnum.ACTIVE.getValue());
		List<VehicleConfig> vehicleConfigList = vehicleConfigMapper.selectConfigByExample(search);
		// 对象转化输出
		return ResultRes.success(convertToConfigRes(vehicleConfigList));
	}

	/**
	 * 查询父节点下车辆子节点
	 * @param parentUuid
	 * @return
	 */
	@Override
	public ResultRes<List<ConfigRes>> queryListByParent(String parentUuid) {
		// 查询所有有效的数据信息
		VehicleConfig search = new VehicleConfig();
		search.setSts(StsEnum.ACTIVE.getValue());
		search.setParentCode(parentUuid);
		List<VehicleConfig> vehicleConfigList = vehicleConfigMapper.selectConfigByExample(search);
		// 对象转化输出
		return ResultRes.success(convertToConfigRes(vehicleConfigList));
	}

	/**
	 * 对象转化
	 * @param vehicleConfigList
	 * @return
	 */
	private List<ConfigRes> convertToConfigRes(List<VehicleConfig> vehicleConfigList) {
		List<ConfigRes> listRes = new ArrayList<>();
		if (!CollectionUtils.isEmpty(vehicleConfigList)) {
			for (VehicleConfig config : vehicleConfigList) {
				ConfigRes res = new ConfigRes();
				BeanUtils.copyProperties(config, res);
				listRes.add(res);
			}
		}
		return listRes;
	}

	/**
	 * 根据uuid查询车辆节点信息
	 * @param uuid
	 * @return
	 */
	@Override
	public ResultRes<ConfigRes> queryConfig(String uuid) {
		VehicleConfig config = vehicleConfigMapper.selectByPrimaryKey(uuid);
		if (StringUtils.isEmpty(config)) {
			throw new BusinessException(ResEnum.NON_EXISTENT);
		}
		ConfigRes res = new ConfigRes();
		BeanUtils.copyProperties(config, res);
		return ResultRes.success(res);
	}

	/**
	 * 批量导入车辆配置信息
	 * @param file
	 * @return
	 */
	@Override
	public ResultRes batchImport(MultipartFile file) {
		boolean isE2007 = false;
		// 判断是否是excel2007格式
		if (file.getOriginalFilename().endsWith("xlsx")) {
			isE2007 = true;
		}
		int rowIndex = 0;
		try {
			// 建立输入流
			InputStream input = file.getInputStream();
			Workbook wb;
			// 根据文件格式(2003或者2007)来初始化
			if (isE2007) {
				wb = new XSSFWorkbook(input);
			} else {
				wb = new HSSFWorkbook(input);
			}
			// 获得第一个表单
			Sheet sheet = wb.getSheetAt(0);
			int rowCount = sheet.getLastRowNum() + 1;
			for (int i = 1; i < rowCount; i++) {
				rowIndex = i;
				String vehiclePinPai = ExcelUtils.getSheetValue(sheet, i, 0);
				String vehicleXingHao = ExcelUtils.getSheetValue(sheet, i, 1);
				if (StringUtils.isEmpty(vehiclePinPai) || StringUtils.isEmpty(vehicleXingHao)) {
					continue;
				}
				// 查询品牌是否已存在
				VehicleConfig vehicleConfig = new VehicleConfig();
				vehicleConfig.setConfigName(vehiclePinPai.trim());
				vehicleConfig.setConfigType("2");
				vehicleConfig.setSts(0);
				vehicleConfig = vehicleConfigMapper.selectOne(vehicleConfig);
				if (StringUtils.isEmpty(vehicleConfig)) {
					// 创建品牌数据
					vehicleConfig = new VehicleConfig();
					vehicleConfig.setUuid(UuidUtils.getUUID());
					vehicleConfig.setConfigName(vehiclePinPai.trim());
					vehicleConfig.setParentCode("0001");
					vehicleConfig.setConfigType("2");
					vehicleConfig.setSts(StsEnum.ACTIVE.getValue());
					vehicleConfig.setCreatedTime(new Date());
					// 查询排序最大的一个品牌
					Integer maxSort = vehicleConfigMapper.selectMaxSortByVehicleConfigBrand();
					vehicleConfig.setSortNum(maxSort + 1);
					vehicleConfigMapper.insert(vehicleConfig);
				}
				// 检查车辆型号是否存在
				VehicleConfig vehicleModel = new VehicleConfig();
				vehicleModel.setConfigName(vehicleXingHao.trim());
				vehicleModel.setConfigType("3");
				vehicleModel.setSts(0);
				vehicleModel.setParentCode(vehicleConfig.getUuid());
				vehicleModel = vehicleConfigMapper.selectOne(vehicleModel);
				if (!StringUtils.isEmpty(vehicleModel)) {
					// 已存在当前车辆，跳过执行
					continue;
				}
				// 新增当前车辆
				vehicleModel = new VehicleConfig();
				vehicleModel.setUuid(UuidUtils.getUUID());
				vehicleModel.setConfigName(vehicleXingHao.trim());
				vehicleModel.setParentCode(vehicleConfig.getUuid());
				vehicleModel.setSts(StsEnum.ACTIVE.getValue());
				vehicleModel.setConfigType("3");
				vehicleModel.setCreatedTime(new Date());
				// 查询排序最大的一个型号
				Integer maxSort = vehicleConfigMapper.selectMaxSortByVehicleConfigModel(vehicleConfig.getUuid());
				vehicleModel.setSortNum(maxSort + 1);
				vehicleConfigMapper.insert(vehicleModel);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ResultRes.success();
	}

	@Override
	public List<ConfigRes> queryListByUuid(List<String> uuidList) {
		List<ConfigRes> reslist = new ArrayList<>();
		List<VehicleConfig> list = vehicleConfigMapper.queryListByUuid(uuidList);
		for (VehicleConfig vehicleConfig : list) {
			ConfigRes re = new ConfigRes();
			BeanUtils.copyProperties(vehicleConfig, re);
			reslist.add(re);
		}
		return reslist;
	}
}
