package com.car.system.web.service;

import com.car.common.res.ResultRes;
import com.car.system.client.response.dict.DictionaryRes;

import java.util.List;

/**
 *
 * @author xlj
 */
public interface SysDictService {
    /**
     * 根据字典类型查询字典集合
     * @param type
     * @return
     */
    ResultRes<List<DictionaryRes>> queryListByType(String type);

    /**
     * 根据字典编码查询字典信息
     * @param code
     * @return
     */
    ResultRes<DictionaryRes> queryByCode(String code);

    /**
     * 根据字典ID查询字典信息
     * @param uuid
     * @return
     */
    ResultRes<DictionaryRes> queryByUuid(String uuid);

    /**
     * 根据字典名称查询字典ID
     * @param descName
     * @return
     */
    ResultRes<String> queryByDescName(String descName);
}
