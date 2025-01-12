package com.car.system.web.controller;

import com.car.common.res.ResultRes;
import com.car.system.client.response.dict.DictionaryRes;
import com.car.system.web.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理
 * @author xlj
 */
@RestController
@RequestMapping("/dict")
@Slf4j
@Api(value = "DictionaryController", tags = "字典管理")
public class DictController {

    @Autowired
    SysDictService sysDictService;

    @GetMapping(value = "/queryListByType/{type}")
    @ApiOperation(value = "根据字典类型查询字典集合", nickname = "queryListByType")
    public ResultRes<List<DictionaryRes>> queryListByType(@PathVariable(name = "type") String type) {
        return sysDictService.queryListByType(type);
    }

    @GetMapping(value = "/queryByCode/{code}")
    @ApiOperation(value = "根据字典编码查询字典信息", nickname = "queryByCode")
    public ResultRes<DictionaryRes> queryByCode(@PathVariable(name = "code") String code) {
        return sysDictService.queryByCode(code);
    }

    @GetMapping(value = "/queryByUuid/{uuid}")
    @ApiOperation(value = "根据字典ID查询字典信息", nickname = "queryByUuid")
    public ResultRes<DictionaryRes> queryByUuid(@PathVariable(name = "uuid") String uuid) {
        return sysDictService.queryByUuid(uuid);
    }

    @GetMapping(value = "/queryByDescName")
    @ApiOperation(value = "根据字典描述查询字典信息", nickname = "queryByDescName")
    public ResultRes<String> queryByDescName(@RequestParam(name = "descName",required = true) String descName){
        return sysDictService.queryByDescName(descName);
    }

}

