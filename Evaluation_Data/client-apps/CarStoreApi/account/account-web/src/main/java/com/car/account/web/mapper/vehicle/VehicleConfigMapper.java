package com.car.account.web.mapper.vehicle;

import com.car.account.client.response.vehicle.config.ConfigRes;
import com.car.account.web.model.vehicle.VehicleConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author xlj
 */
@Repository
public interface VehicleConfigMapper extends Mapper<VehicleConfig> {

	/**
	 * 根据条件查询配置信息
	 * @param search
	 * @return
	 */
	List<VehicleConfig> selectConfigByExample(VehicleConfig search);

	/**
	 * 查询排序最大的一个品牌
	 * @return
	 */
	Integer selectMaxSortByVehicleConfigBrand();

	/**
	 * 查询排序最大的一个型号，某个品牌下
	 * @param brandUuid
	 * @return
	 */
	Integer selectMaxSortByVehicleConfigModel(@Param("brandUuid") String brandUuid);

	List<VehicleConfig> queryListByUuid(@Param("uuidList") List<String> uuidList);
}
