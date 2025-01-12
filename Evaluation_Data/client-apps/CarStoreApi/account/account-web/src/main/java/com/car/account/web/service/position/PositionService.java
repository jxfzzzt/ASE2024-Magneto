package com.car.account.web.service.position;

import com.car.account.client.request.position.PositionReq;
import com.car.account.client.response.position.PositionRes;
import com.car.common.res.ResultRes;

/**
 * @author lj.x
 * @date 2021/1/16 21:10
 */
public interface PositionService {

	/**
	 * 上报用户位置信息（技师/店铺）
	 * @param param
	 * @return
	 */
	ResultRes<String> reportPosition(PositionReq param);

	/**
	 * 拉取用户位置信息（技师/车主）
	 * @param param
	 * @return
	 */
	ResultRes<PositionRes> queryPosition();
}
