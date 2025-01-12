package com.car.system.web.service;

import com.car.common.res.ResultRes;
import com.car.system.client.request.area.AreaReq;
import com.car.system.client.response.area.AreaRes;

import java.util.List;

/**
 * @author xlj
 */
public interface SysAreaService {

    /**
     * 查询父节点所有地区，根节点传-1
     * @param parentUuid
     * @return
     */
    ResultRes<List<AreaRes>> queryListByParent(String parentUuid);

    /**
     * 根据uuid查询地区信息
     * @param uuid
     * @return
     */
    ResultRes<AreaRes> queryArea(String uuid);

    /**
     * 根据名称查询地区信息
     * @param name
     * @param areaType
     * @return
     */
    ResultRes<AreaRes> queryAreaName(String name, Integer areaType);

    /**
     * 查询所有区域信息
     * @return
     */
    ResultRes<List<AreaRes>> queryList(Integer areaType);

    /**
     * 查询地区
     * @param params
     * @return
     */
    ResultRes<List<AreaRes>> queryAreaList(AreaReq params);

    /**
     * 根据经纬度查询uuid
     * @return
     */
    public ResultRes<String> getAreaUuid(String lat,String lng);
}
