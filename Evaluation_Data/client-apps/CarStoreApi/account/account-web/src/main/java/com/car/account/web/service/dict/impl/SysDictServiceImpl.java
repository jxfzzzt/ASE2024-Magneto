package com.car.account.web.service.dict.impl;

import com.car.account.web.service.dict.SysDictService;
import com.car.common.res.ResultRes;
import com.car.common.utils.StringUtil;
import com.car.system.client.feign.SystemFeign;
import com.car.system.client.response.dict.DictionaryRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangyp
 * @date 2021/1/28 0:00
 */
@Slf4j
@Service
public class SysDictServiceImpl implements SysDictService {

    @Resource
    SystemFeign systemFeign;

    @Override
    public DictionaryRes querySysDict(String uuid) {

        if(StringUtil.isBlank(uuid)){
            return null;
        }

        log.info("查询字典数据>>>uuid:{}",uuid);
        ResultRes<DictionaryRes> dictionaryResResultRes = systemFeign.queryByUuid(uuid);
        if(dictionaryResResultRes.isSuccess()){
            DictionaryRes data = dictionaryResResultRes.getData();
            return data;
        }
        return null;
    }
}
