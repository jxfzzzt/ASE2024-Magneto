package com.car.account.web.service.dict;

import com.car.system.client.response.dict.DictionaryRes;

/**
 * 系统字典表查询
 * @author zhangyp
 * @date 2021/1/27 23:59
 */
public interface SysDictService {

    DictionaryRes querySysDict(String uuid);
}
