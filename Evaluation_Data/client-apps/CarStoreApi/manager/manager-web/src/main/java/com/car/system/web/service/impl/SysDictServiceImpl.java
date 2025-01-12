package com.car.system.web.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.car.common.enums.ResEnum;
import com.car.common.enums.StsEnum;
import com.car.common.exception.BusinessException;
import com.car.common.res.ResultRes;
import com.car.common.utils.Constants;
import com.car.common.utils.RedisUtils;
import com.car.system.client.response.dict.DictionaryRes;
import com.car.system.web.mapper.SysDictMapper;
import com.car.system.web.model.SysDict;
import com.car.system.web.service.SysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author xlj
 */
@Service
@Slf4j
public class SysDictServiceImpl implements SysDictService {


    @Autowired
    private SysDictMapper sysDictMapper;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 根据字典类型查询字典集合
     *
     * @param type
     * @return
     */
    @Override
    public ResultRes<List<DictionaryRes>> queryListByType(String type) {
        List<DictionaryRes> dicListRes = new ArrayList<>();
        //获取缓存
        String cacheKey = String.format(Constants.queryDictListByType_cache_key, new Object[] { type });
        String redisValue = (String) redisUtils.get(cacheKey);
        if(!StringUtils.isEmpty(redisValue)){
            dicListRes = JSONArray.parseArray(redisValue, DictionaryRes.class);
            return ResultRes.success(dicListRes);
        }
        SysDict search = new SysDict();
        search.setLableType(type);
        search.setSts(StsEnum.ACTIVE.getValue());
        List<SysDict> dicList = sysDictMapper.select(search);
        for (SysDict dic : dicList) {
            DictionaryRes res = new DictionaryRes();
            BeanUtils.copyProperties(dic, res);
            dicListRes.add(res);
        }
        Collections.sort(dicListRes, Comparator.comparing(DictionaryRes::getSortNum));
        redisUtils.set(cacheKey,JSONArray.toJSONString(dicListRes),5L, TimeUnit.MINUTES);
        return ResultRes.success(dicListRes);
    }

    /**
     * 根据字典编码查询字典信息
     *
     * @param code
     * @return
     */
    @Override
    public ResultRes<DictionaryRes> queryByCode(String code) {
        SysDict search = new SysDict();
        search.setLableCode(code);
        search.setSts(StsEnum.ACTIVE.getValue());
        SysDict dic = sysDictMapper.selectOne(search);
        if (StringUtils.isEmpty(dic)) {
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        DictionaryRes res = new DictionaryRes();
        BeanUtils.copyProperties(dic, res);
        return ResultRes.success(res);
    }

    /**
     * 根据字典ID查询字典信息
     *
     * @param uuid
     * @return
     */
    @Override
    public ResultRes<DictionaryRes> queryByUuid(String uuid) {
        //获取缓存
        String cacheKey = String.format(Constants.queryDictDetailByUuid_cache_key, new Object[] { uuid });
        String redisValue = (String) redisUtils.get(cacheKey);
        if(!StringUtils.isEmpty(redisValue)){
            DictionaryRes dic = JSONArray.parseObject(redisValue, DictionaryRes.class);
            return ResultRes.success(dic);
        }
        SysDict dic = sysDictMapper.selectByPrimaryKey(uuid);
        if (StringUtils.isEmpty(dic)) {
            throw new BusinessException(ResEnum.NON_EXISTENT);
        }
        DictionaryRes res = new DictionaryRes();
        BeanUtils.copyProperties(dic, res);
        redisUtils.set(cacheKey,JSONArray.toJSONString(res),5L, TimeUnit.MINUTES);
        return ResultRes.success(res);
    }

    @Override
    public ResultRes<String> queryByDescName(String descName) {
        SysDict dic = sysDictMapper.selectLikeDescName(descName);
        if (StringUtils.isEmpty(dic)){
            return ResultRes.error(ResEnum.DIC_LAB_VALUE_ERROR.getValue(),ResEnum.DIC_LAB_VALUE_ERROR.getDesc());
        }
        return ResultRes.success(dic.getUuid());
    }
}
