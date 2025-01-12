package com.car.account.web.service.vehicle;

import com.car.account.client.response.vehicle.config.ConfigRes;
import com.car.common.res.ResultRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xlj
 */
public interface VehicleConfigService {

	/**
	 * 查询所有车辆配置信息
	 * @return
	 */
	ResultRes<List<ConfigRes>> queryAllList();

	/**
	 * 查询父节点下车辆子节点
	 * @param parentUuid
	 * @return
	 */
	ResultRes<List<ConfigRes>> queryListByParent(String parentUuid);

	/**
	 * 根据uuid查询车辆节点信息
	 * @param uuid
	 * @return
	 */
	ResultRes<ConfigRes> queryConfig(String uuid);

	/**
	 * 批量导入车辆配置信息
	 * @param file
	 * @return
	 */
	ResultRes batchImport(MultipartFile file);

	/**
	 * 查询车辆类型信息集合
	 * @param uuidList
	 * @return
	 */
	List<ConfigRes> queryListByUuid(List<String> uuidList);

}
