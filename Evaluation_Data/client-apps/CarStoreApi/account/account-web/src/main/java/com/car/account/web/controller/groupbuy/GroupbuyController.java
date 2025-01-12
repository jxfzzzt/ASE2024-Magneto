package com.car.account.web.controller.groupbuy;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.car.account.client.request.groupbuy.AddGroupbuyReq;
import com.car.account.client.request.groupbuy.QueryGroupbuyListReq;
import com.car.account.client.request.groupbuy.UpdateGroupbuyReq;
import com.car.account.client.response.groupbuy.GroupbuyRes;
import com.car.account.web.service.groupby.GroupbuyService;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jinhaifei
 * @date 2021/07/12
 */
@Slf4j
@Api(value = "GroupbuyController", tags = "团购管理")
@RequestMapping(value = "/groupbuy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class GroupbuyController {

	@Autowired
	private GroupbuyService groupbuyService;

	@PostMapping("addGroupbuy")
	@ApiOperation("新增团购")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "团购管理", operType = OperEnum.ADD, operDesc = "新增团购")
	public ResultRes<String> addGroupbuy(@RequestBody @Valid AddGroupbuyReq addGroupbuyReq) {
		return groupbuyService.add(addGroupbuyReq);
	}

	@PostMapping("updateGroupbuy")
	@ApiOperation("更新团购")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "团购管理", operType = OperEnum.UPDATE, operDesc = "更新团购")
	public ResultRes<String> updateGroupbuy(@RequestBody @Valid UpdateGroupbuyReq updateGroupbuyReq) {
		return groupbuyService.update(updateGroupbuyReq);
	}

	@PostMapping("deleteGroupbuy/{uuid}")
	@ApiOperation("删除团购")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = true, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "团购管理", operType = OperEnum.DELETE, operDesc = "删除团购")
	public ResultRes<String> deleteGroupbuy(@PathVariable(name = "uuid") String uuid) {
		return groupbuyService.delete(uuid);
	}

	@PostMapping("queryGroupbuy/{uuid}")
	@ApiOperation("查询单条团购")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "团购管理", operType = OperEnum.SELECT, operDesc = "查询单条团购")
	public ResultRes<GroupbuyRes> queryGroupbuy(@PathVariable(name = "uuid") String uuid) {
		return groupbuyService.queryByUuid(uuid);
	}

	@PostMapping("queryGroupbuyList")
	@ApiOperation("查询团购列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header") })
	@SysOperLog(operModul = "团购管理", operType = OperEnum.SELECT, operDesc = "查询团购列表")
	public PageRes<List<GroupbuyRes>> queryGroupbuyList(@RequestBody @Valid QueryGroupbuyListReq queryGroupbuyListReq) {
		return groupbuyService.queryGroupbuyList(queryGroupbuyListReq);
	}

}
