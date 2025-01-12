package com.car.account.web.mapper.contact;

import com.car.account.client.request.contact.QueryContactListReq;
import com.car.account.client.response.contact.ContactRes;
import com.car.account.web.model.contact.ContactMsg;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author zhouz
 * @date 2020/12/22
 */
@Repository
public interface ContactMapper extends Mapper<ContactMsg> {

    /**
     * 查询数据列表
     * @param param
     * @return
     */
    List<ContactRes> queryList(QueryContactListReq param);
}
