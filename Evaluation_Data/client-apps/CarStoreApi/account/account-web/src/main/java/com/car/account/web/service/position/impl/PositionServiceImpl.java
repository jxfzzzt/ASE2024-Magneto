package com.car.account.web.service.position.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.car.account.client.request.position.PositionReq;
import com.car.account.client.response.position.PositionRes;
import com.car.account.web.mapper.store.StoreMapper;
import com.car.account.web.mapper.store.StoreUserMapper;
import com.car.account.web.mapper.technician.TechnicianMapper;
import com.car.account.web.mapper.vehicle.VehicleUserMapper;
import com.car.account.web.model.technician.Technician;
import com.car.account.web.model.vehicle.VehicleUser;
import com.car.account.web.service.position.PositionService;
import com.car.common.enums.UserTypeEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.TokenHelper;
import com.car.common.utils.token.Baggages;
import com.car.common.utils.token.LoginToken;
import com.car.utility.client.feign.BaiduFeign;
import com.car.utility.client.response.BaiDuPositionRes;
import com.car.utility.client.response.GetBaiDuCoordinateByGPSRes;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangyp
 * @date 2021/1/16 21:28
 */
@Slf4j
@Service
public class PositionServiceImpl implements PositionService {

	@Resource
	private TechnicianMapper technicianMapper;
	@Resource
	private StoreMapper storeMapper;
	@Resource
	private StoreUserMapper storeUserMapper;
	@Autowired
	private VehicleUserMapper vehicleUserMapper;

	@Autowired
	private BaiduFeign baiduFeign;

	/**
	 * 拉取用户位置信息（技师/车主）
	 * @param param
	 * @return
	 */
	@Override
	public ResultRes<PositionRes> queryPosition() {
		log.info(Baggages.getToken());
		LoginToken loginToken = TokenHelper.getLoginToken();
		PositionRes res = new PositionRes();

		if (UserTypeEnum.technician.getType().equals(loginToken.getUserType())) {

			Technician technician = technicianMapper.selectByPrimaryKey(loginToken.getUserUuid());
			res.setLatitude(technician.getAddressLatitude());
			res.setLongitude(technician.getAddressLongitude());

		} else if (UserTypeEnum.vehicle.getType().equals(loginToken.getUserType())) {

			VehicleUser vehicleUser = vehicleUserMapper.selectByPrimaryKey(loginToken.getUserUuid());
			res.setLatitude(vehicleUser.getLatitude());
			res.setLongitude(vehicleUser.getLongitude());

		}
		return ResultRes.success(res);
	}

	/**
	 * 上报用户位置信息（技师/店铺）
	 * @param param
	 * @return
	 */
	@Override
	public ResultRes<String> reportPosition(PositionReq param) {
		log.info(Baggages.getToken());
		LoginToken loginToken = TokenHelper.getLoginToken();
		if (UserTypeEnum.technician.getType().equals(loginToken.getUserType())) {
			// 更新技师的经纬度信息
			Float[] coordinate = getBaiduCoordinate(param.getLatitude(), param.getLongitude());
			if (coordinate == null) {
				return ResultRes.success(loginToken.getUserUuid());
			}
			Technician technician = technicianMapper.selectByPrimaryKey(loginToken.getUserUuid());
			technician.setAddressLatitude(coordinate[0]);
			technician.setAddressLongitude(coordinate[1]);
			technician.setLastUpdatedTime(new Date());
			technician.setLastUpdatedBy(loginToken.getUserName());
			technicianMapper.updateByPrimaryKey(technician);
		} else if (UserTypeEnum.store.getType().equals(loginToken.getUserType())) {
			// 更新店铺的经纬度信息
			/*StoreUser storeUser = storeUserMapper.selectByPrimaryKey(loginToken.getUserUuid());
			if(StringUtils.isEmpty(storeUser)){
			    throw new BusinessException(ResEnum.NON_EXISTENT);
			}
			//查询店铺信息
			Store store = storeMapper.selectByPrimaryKey(storeUser.getStoreUuid());
			store.setLatitude(param.getLatitude());
			store.setLongitude(param.getLongitude());
			store.setLastUpdatedTime(new Date());
			store.setLastUpdatedBy(loginToken.getUserName());
			storeMapper.updateByPrimaryKey(store);*/
		} else if (UserTypeEnum.vehicle.getType().equals(loginToken.getUserType())) {
			// 记录用户的经纬度信息
			VehicleUser vehicleUser = vehicleUserMapper.selectByPrimaryKey(loginToken.getUserUuid());
			Float[] coordinate = getBaiduCoordinate(param.getLatitude(), param.getLongitude());
			if (coordinate == null) {
				return ResultRes.success(loginToken.getUserUuid());
			}
			vehicleUser.setLatitude(coordinate[0]);
			vehicleUser.setLongitude(coordinate[1]);
			vehicleUserMapper.updateByPrimaryKey(vehicleUser);
		}
		return ResultRes.success(loginToken.getUserUuid());
	}

	/**
	 * 获取百度坐标经纬度
	 * @param latitude  纬度
	 * @param longitude 经度
	 * @return
	 */
	private Float[] getBaiduCoordinate(Float latitude, Float longitude) {
		ResultRes<GetBaiDuCoordinateByGPSRes> getBaiDuCoordinateByGPSResResultRes = baiduFeign
				.getBaiDuCoordinateByGPS(latitude + "," + longitude);
		if (getBaiDuCoordinateByGPSResResultRes.isSuccess()) {
			GetBaiDuCoordinateByGPSRes getBaiDuCoordinateByGPSRes = getBaiDuCoordinateByGPSResResultRes.getData();
			List<BaiDuPositionRes> baiDuPositionResList = getBaiDuCoordinateByGPSRes.getResult();
			if (!CollectionUtils.isEmpty(baiDuPositionResList)) {
				Float[] coordinate = new Float[2];
				coordinate[0] = baiDuPositionResList.get(0).getX();
				coordinate[1] = baiDuPositionResList.get(0).getY();
				return coordinate;
			}
		}
		return null;
	}
}
