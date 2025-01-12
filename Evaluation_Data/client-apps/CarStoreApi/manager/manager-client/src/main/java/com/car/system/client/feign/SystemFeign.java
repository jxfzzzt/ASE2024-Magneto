package com.car.system.client.feign;


import com.car.common.annotation.SysOperLog;
import com.car.common.constant.ServiceNameConstant;
import com.car.common.enums.OperEnum;
import com.car.common.req.SysOperationLogReq;
import com.car.common.res.ResultRes;
import com.car.system.client.response.area.AreaRes;
import com.car.system.client.response.dict.DictionaryRes;
import com.car.system.client.response.menu.SysMenuUrlRes;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Buerger
 * @date 2020/6/15 11:47
 */
@FeignClient(value = ServiceNameConstant.SYSTEM)
public interface SystemFeign{

    /**
     * 通过角色ID获取角色信息
     * @param uuid  角色id
     * @return  角色菜单列表
     */
    @GetMapping(value = "/menuUrl/queryMenuUrlByRoleId/{uuid}")
    ResultRes<List<SysMenuUrlRes>> queryMenuUrlByRoleId(@PathVariable("uuid") String uuid);


    /**
     * 保存系统操作日志
     * @param param  请求参数
     * @return 返回日志id
     */
    @PostMapping("/log/insertSysLog")
    ResultRes<String> insertSysLog(@RequestBody SysOperationLogReq param);

    /**
     * 根据字典类型查询字典集合
     * @param type
     * @return
     */
    @GetMapping(value = "/dict/queryListByType/{type}")
    public ResultRes<List<DictionaryRes>> queryListByType(@PathVariable(name = "type") String type);

    /**
     * 根据字典编码查询字典信息
     * @param code
     * @return
     */
    @GetMapping(value = "/dict/queryByCode/{code}")
    public ResultRes<DictionaryRes> queryByCode(@PathVariable(name = "code") String code);

    /**
     * 根据字典ID查询字典信息
     * @param uuid
     * @return
     */
    @GetMapping(value = "/dict/queryByUuid/{uuid}")
    public ResultRes<DictionaryRes> queryByUuid(@PathVariable(name = "uuid") String uuid);

    /**
     * 根据字典描述查询字典信息
     * @param descName
     * @return
     */
    @GetMapping(value = "/dict/queryByDescName")
    public ResultRes<String> queryByDescName(@RequestParam(name = "descName",required = true) String descName);

    /**
     * 根据ID查询地址信息
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/area/queryArea/{uuid}", method = RequestMethod.GET)
    public ResultRes<AreaRes> queryArea(@PathVariable(name = "uuid") String uuid);
}
