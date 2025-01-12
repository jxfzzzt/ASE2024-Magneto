package com.car.system.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.res.ResultRes;
import com.car.common.utils.Constants;
import com.car.common.utils.RedisUtils;
import com.car.common.utils.StringUtil;
import com.car.system.client.request.area.AreaReq;
import com.car.system.client.response.area.AreaRes;
import com.car.system.client.response.area.AddressRes;
import com.car.system.web.mapper.SysAreaMapper;
import com.car.system.web.model.SysArea;
import com.car.system.web.service.SysAreaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author xlj
 */
@Slf4j
@Service
public class SysAreaServiceImpl implements SysAreaService {

    @Autowired
    SysAreaMapper sysAreaMapper;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 查询父节点所有地区，根节点传-1
     *
     * @param parentUuid
     * @return
     */
    @Override
    public ResultRes<List<AreaRes>> queryListByParent(String parentUuid) {
        String cacheKey = String.format(Constants.SYS_AREA_QUERY_LIST_BY_PARENT_CACHE_KEY, new Object[] { parentUuid });
        String redisValue = (String) redisUtils.get(cacheKey);
        //开始接收缓存参数
        List<AreaRes> areaResList = new ArrayList<>();
        if(!StringUtils.isEmpty(redisValue)){
            areaResList = JSONArray.parseArray(redisValue,AreaRes.class);
            return ResultRes.success(areaResList);
        }
        SysArea search = new SysArea();
        search.setParentUuid(parentUuid);
        search.setSts(StsEnum.ACTIVE.getValue());
        List<SysArea> areaList = sysAreaMapper.queryListByParent(search);
        //封装返回输出
        for (SysArea area : areaList) {
            AreaRes res = new AreaRes();
            BeanUtils.copyProperties(area, res);
            areaResList.add(res);
        }
        //设置数据缓存时间
        redisUtils.set(cacheKey,JSONArray.toJSONString(areaResList),5L, TimeUnit.MINUTES);
        return ResultRes.success(areaResList);
    }

    /**
     * 根据ID查询区域信息
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<AreaRes> queryArea(String uuid) {
        SysArea area = sysAreaMapper.selectByPrimaryKey(uuid);
        AreaRes res = new AreaRes();
        if (!StringUtils.isEmpty(area)) {
            BeanUtils.copyProperties(area, res);
        }
        return ResultRes.success(res);
    }

    /**
     * 根据区域名字查询区域信息
     * @param name
     * @param areaType
     * @return
     */
    @Override
    public ResultRes<AreaRes> queryAreaName(String name, Integer areaType) {
        List<SysArea> sysArea1 = sysAreaMapper.queryAreaNameAreaType(name, areaType);
        AreaRes areaRes = new AreaRes();
        if (sysArea1.size() <= 1) {
            BeanUtils.copyProperties(sysArea1.get(0), areaRes);
        } else {
            return ResultRes.error(ResEnum.AREA_NAME_TYPE_ERROR.getValue(), ResEnum.AREA_NAME_TYPE_ERROR.getDesc());
        }
        return ResultRes.success(areaRes);
    }

    /**
     * 查询所有区域信息
     * @return
     */
    @Override
    public ResultRes<List<AreaRes>> queryList(Integer areaType) {
        String cacheKey = String.format(Constants.SYS_AREA_QUERY_LIST_CACHE_KEY,areaType);
        String redisValue = (String) redisUtils.get(cacheKey);
        //开始接收缓存参数
        List<AreaRes> areaResList = new ArrayList<>();
        if(!StringUtils.isEmpty(redisValue)){
            areaResList = JSONArray.parseArray(redisValue,AreaRes.class);
            return ResultRes.success(areaResList);
        }
        SysArea search = new SysArea();
        search.setSts(StsEnum.ACTIVE.getValue());
        if (!StringUtils.isEmpty(areaType)) {
            search.setAreaType(areaType);
        }
        List<SysArea> areaList = sysAreaMapper.select(search);
        //封装返回输出
        for (SysArea area : areaList) {
            AreaRes res = new AreaRes();
            BeanUtils.copyProperties(area, res);
            areaResList.add(res);
        }
        //设置数据缓存时间
        redisUtils.set(cacheKey,JSONArray.toJSONString(areaResList),5L, TimeUnit.MINUTES);
        return ResultRes.success(areaResList);
    }

    @Override
    public ResultRes<List<AreaRes>> queryAreaList(AreaReq params) {

        List<AreaRes> areaRes = sysAreaMapper.queryAreaList(params);
        return ResultRes.success(areaRes);
    }

    /**
     *
     * @return
     */
    @Override
    public ResultRes<String> getAreaUuid(String lat,String lng){
        if (StringUtil.isBlank(lat)||StringUtil.isBlank(lng)){
            return ResultRes.error("定位获取异常");
        }
        AddressRes addressRes = getAddres(lat,lng);
        SysArea sysArea = new SysArea();
        sysArea.setAreaCode( addressRes.getAdcode());
        sysArea = sysAreaMapper.selectOne(sysArea);
        return  ResultRes.success(sysArea.getUuid());
    }

    private  AddressRes getAddres(String lat, String lng) {
        String appKey ="uda80hknhokVvMa6gHt5xOaRx1sTbcL9";
        StringBuilder path = new StringBuilder()
                .append("http://api.map.baidu.com/reverse_geocoding/v3/?ak=")
                .append(appKey).append("&location=").append(lat).append(",").append(lng).append("&output=json");
        HttpURLConnection con = null;
        try {
            URL url = new URL(path.toString());
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            con.setRequestProperty("Referer", "http://api.map.baidu.com/reverse_geocoding/v3/");
            String b = "0",c= "{",d="status";
            String responseBody = new Scanner(con.getInputStream(), "utf-8").useDelimiter("\\A").next();
            log.error("getGDAddress: res=" + responseBody);

            JSONObject jsonObject = JSONObject.parseObject(responseBody);

            if (!b.equals(jsonObject.getString(d))) {
                throw new RuntimeException("根据经纬度获取详细地址失败");
            }
            if (!responseBody.startsWith(c)) {
                throw new RuntimeException("根据经纬度获取详细地址失败");
            }
          return   JSONObject.parseObject(jsonObject.getJSONObject("result").getString("addressComponent"), AddressRes.class);
        } catch (Exception e) {
            log.error("get info error,  catch exception info: message=" + e.getMessage());
        }finally {
            if (null != con){
                con.disconnect();
            }
        }
        throw new RuntimeException("根据经纬度获取详细地址失败");
    }



}
