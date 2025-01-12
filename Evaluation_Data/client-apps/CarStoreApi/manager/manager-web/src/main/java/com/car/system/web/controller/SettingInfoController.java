package com.car.system.web.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.request.setting.SettingInfoListReq;
import com.car.system.client.request.setting.SettingInfoReq;
import com.car.system.client.request.setting.SettingInfoUpdateReq;
import com.car.system.web.model.SettingInfo;
import com.car.system.web.service.SettingInfoService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 
* @author
* @version
* 模块配置
*/
@Slf4j
@RestController
@RequestMapping("/setting")
@Api(value = "SettingInfoController", tags = "模块配置Controller")
public class SettingInfoController {
	
	@Autowired
	private SettingInfoService settingInfoService;

	/**
	 * 根据id查询详情
	 */
	@GetMapping("/getById/{uuid}")
	@ApiOperation(value = "根据id查询详情", nickname = "getById")
    @SysOperLog(operModul = "模块配置", operType = OperEnum.SELECT, operDesc = "根据id查询详情")
	public ResultRes<SettingInfo> getById(@PathVariable(name = "uuid" ,required = true) String uuid) {
		return settingInfoService.getById(uuid);
	}
	
	/**
	 * 新增模块配置
	 */
	@PostMapping("/add")
	@ApiOperation(value = "新增模块配置", nickname = "add")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
	})
    @SysOperLog(operModul = "模块配置", operType = OperEnum.ADD, operDesc = "新增模块配置")
	public ResultRes<String> add(@RequestBody @Validated SettingInfoReq settingInfoReq) {
		return settingInfoService.add(settingInfoReq);
	}
	
	/**
	 * 修改模块配置
	 */
	@PutMapping("/updateById")
    @ApiOperation(value = "根据id修改模块配置", nickname = "updateById")
    @SysOperLog(operModul = "模块配置", operType = OperEnum.UPDATE, operDesc = "根据id修改模块配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "特权标识", required = false, dataType = "String" ,paramType="header")
    })
	public ResultRes updateById(@RequestBody @Validated SettingInfoUpdateReq settingInfoUpdateReq) {
		return settingInfoService.updateById(settingInfoUpdateReq);
	}
	
	/**
	 * 删除模块配置
	 */
	@DeleteMapping(value = "/deleteById/{uuid}")
    @ApiOperation(value = "删除模块配置", nickname = "deleteById")
    @SysOperLog(operModul = "模块配置", operType = OperEnum.DELETE, operDesc = "删除模块配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "token", required = false, dataType = "String", paramType = "header")
    })
	public ResultRes deleteById(@PathVariable("uuid") String uuid) {
		return settingInfoService.deleteById(uuid);
	}
	
	/**
	 * 根据code获取列表
	 */
	@GetMapping("/getByCode/{code}")
	@ApiOperation(value = "根据code获取列表", nickname = "getByCode")
    @SysOperLog(operModul = "模块配置", operType = OperEnum.SELECT, operDesc = "根据code获取列表")
	public ResultRes<Object> getByCode(@PathVariable(name = "code")
		@ApiParam(name="code",value="配置code说明:guide_page_technician_store 引导页（技师店铺），guide_page_owner 引导页（车主），driving_agent 代驾， consult 付费咨询，technician_case  案例",required=true) String code) {
		return settingInfoService.getByCode(code);
	}
	
	/**
	 * 分页列表
	 */
	@RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "模块配置分页列表", nickname = "list")
    @SysOperLog(operModul = "模块配置", operType = OperEnum.SELECT, operDesc = "模块配置分页列表")
	public PageRes<List<SettingInfo>> list(@RequestBody SettingInfoListReq settingInfoListReq) {
		return settingInfoService.list(settingInfoListReq);
	}


}
