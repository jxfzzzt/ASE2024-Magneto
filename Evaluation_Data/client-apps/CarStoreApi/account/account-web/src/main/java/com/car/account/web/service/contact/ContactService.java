package com.car.account.web.service.contact;

import com.car.account.client.request.contact.ContactReq;
import com.car.account.client.request.contact.QueryContactListReq;
import com.car.account.client.response.contact.ContactRes;
import com.car.common.res.PageRes;
import com.car.common.res.ResultRes;
import com.car.system.client.response.dict.DictionaryRes;

import java.util.List;

/**
 *
 * @author xielinjiang
 * @date 2021/1/27 23:59
 */
public interface ContactService {

    /**
     * 新增留言
     * @param param
     * @return
     */
    ResultRes<String> insertMsg(ContactReq param);

    /**
     * 查询数据列表
     * @param param
     * @return
     */
    PageRes<List<ContactRes>> queryList(QueryContactListReq param);
}
