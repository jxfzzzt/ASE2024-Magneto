package com.car.system.web.controller;

import com.car.common.annotation.SysOperLog;
import com.car.common.enums.OperEnum;
import com.car.common.res.ResultRes;
import com.car.system.client.request.area.AreaReq;
import com.car.system.client.response.area.AreaRes;
import com.car.system.web.service.SysAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地区管理
 * @author xlj
 */
@Slf4j
@Api(value = "AreaController", tags = "地区管理")
@RequestMapping(value = "/area", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class AreaController {

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 查询所有区域信息
     * @return
     */
    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
    @ApiOperation(value = "默认查询所有区域信息 areaType地区级别（1:省份province,2:市city,3:区县district,4:街道street）", nickname = "queryList")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "查询所有区域信息")
    public ResultRes<List<AreaRes>> queryList(Integer areaType) {
        return sysAreaService.queryList(areaType);
    }

    /**
     * 查询父节点所有地区
     * @return
     */
    @RequestMapping(value = "/queryList/{parentUuid}", method = RequestMethod.GET)
    @ApiOperation(value = "查询父节点所有地区，根节点传-1", nickname = "queryListByParent")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "查询父节点所有地区")
    public ResultRes<List<AreaRes>> queryListByParent(@PathVariable(name = "parentUuid") String parentUuid) {
        return sysAreaService.queryListByParent(parentUuid);
    }


    @RequestMapping(value = "/queryArea/{uuid}", method = RequestMethod.GET)
    @ApiOperation(value = "根据uuid查询地区信息", nickname = "queryArea")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "根据uuid查询地区信息")
    public ResultRes<AreaRes> queryArea(@PathVariable(name = "uuid") String uuid) {
        return sysAreaService.queryArea(uuid);
    }

    @RequestMapping(value = "/queryAreaName/{name}/{areaType}", method = RequestMethod.GET)
    @ApiOperation(value = "根据名称查询地区信息", nickname = "queryAreaName")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "根据名称查询地区信息")
    public ResultRes<AreaRes> queryAreaName(@PathVariable(name = "name") String name, @PathVariable(name = "areaType") Integer areaType) {
        return sysAreaService.queryAreaName(name, areaType);
    }

    /**
     * 查询所有区域信息
     * @return
     */
    @RequestMapping(value = "/queryAreaList", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有区域信息", nickname = "queryAreaList")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "查询所有区域信息")
    public ResultRes<List<AreaRes>> queryAreaList(AreaReq params) {
        return sysAreaService.queryAreaList(params);
    }

    @GetMapping("/getAreaUuid")
    @ApiOperation(value = "根据经纬度返回uuid", nickname = "queryAreaList")
    @SysOperLog(operModul = "地区管理", operType = OperEnum.SELECT, operDesc = "查询所有区域信息")
    public ResultRes<String> getAreaUuid(String lat,String lng){
        return  sysAreaService.getAreaUuid( lat, lng);
    }
}
