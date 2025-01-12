package com.car.account.web.controller.position;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.account.client.request.position.PositionReq;
import com.car.account.client.response.position.PositionRes;
import com.car.account.web.service.position.PositionService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lj.x
 * @date 2020/12/22
 */
@Slf4j
@Api(value = "PositionController", tags = "位置管理")
@RequestMapping(value = "/position", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class PositionController {

	@Autowired
	PositionService positionService;

	/**
	 * 上报用户位置信息（技师、店铺）
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/report")
	@ApiOperation(value = "上报用户位置信息（技师/车主）", nickname = "report")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "位置管理", operType = OperEnum.ADD, operDesc = "上报用户位置信息")
	public ResultRes<String> reportPosition(@RequestBody @Valid PositionReq param) {
		return positionService.reportPosition(param);
	}

	/**
	 * 上报用户位置信息（技师、店铺）
	 * @param param
	 * @return
	 */
	@PostMapping(value = "/query")
	@ApiOperation(value = "拉取用户位置信息（技师/车主）", nickname = "query")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "位置管理", operType = OperEnum.SELECT, operDesc = "拉取用户位置信息")
	public ResultRes<PositionRes> queryPosition() {
		return positionService.queryPosition();
	}

}
